package rw.ac.auca.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.homeprep.HomePrepCategory;
import rw.ac.auca.homeprep.HomePreparation;
import rw.ac.auca.homeprep.HomePrepRepository;
import rw.ac.auca.homeprep.dto.HomePrepResponse;
import rw.ac.auca.template.dto.ApplyTemplateRequest;
import rw.ac.auca.template.dto.TemplateResponse;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TemplateService {

    private final PlanningTemplateRepository templateRepository;
    private final HomePrepRepository homePrepRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;

    @Transactional(readOnly = true)
    public List<TemplateResponse> getAllActiveTemplates() {
        return templateRepository.findByIsActiveTrue()
                .stream()
                .map(TemplateResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public TemplateResponse getTemplateById(Long id) {
        PlanningTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
        return TemplateResponse.fromEntity(template);
    }

    public List<HomePrepResponse> applyTemplateToWedding(Long templateId, ApplyTemplateRequest request, User currentUser) {
        PlanningTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + templateId));

        Wedding wedding = resolveWeddingForUser(request != null ? request.getWeddingId() : null, currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        WeddingSide targetSide = (request != null && request.getSide() != null) ? request.getSide() : userSide;

        List<HomePreparation> createdItems = new ArrayList<>();

        for (TemplateItem item : template.getItems()) {
            HomePrepCategory category = parseCategory(item.getItemType());
            WeddingSide itemSide = targetSide != null ? targetSide : item.getDefaultSide();

            HomePreparation prep = HomePreparation.builder()
                    .wedding(wedding)
                    .category(category)
                    .itemName(item.getTitle())
                    .side(itemSide)
                    .budgetRwf(item.getEstimatedBudget())
                    .isCompleted(false)
                    .build();

            createdItems.add(homePrepRepository.save(prep));
        }

        return createdItems.stream()
                .map(HomePrepResponse::fromEntity)
                .toList();
    }

    private HomePrepCategory parseCategory(String rawCategory) {
        if (rawCategory == null) return HomePrepCategory.APPLIANCES;
        try {
            return HomePrepCategory.valueOf(rawCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            return HomePrepCategory.APPLIANCES;
        }
    }

    private Wedding resolveWeddingForUser(Long requestedWeddingId, User currentUser) {
        if (requestedWeddingId != null) {
            return weddingRepository.findById(requestedWeddingId)
                    .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + requestedWeddingId));
        }
        Wedding active = getUserActiveWedding(currentUser);
        if (active == null) {
            throw new IllegalArgumentException("User has no active wedding workspace.");
        }
        return active;
    }

    private Wedding getUserActiveWedding(User currentUser) {
        if (currentUser == null) return null;
        List<WeddingMember> memberships = weddingMemberRepository.findByUserId(currentUser.getId());
        return memberships.isEmpty() ? null : memberships.get(0).getWedding();
    }

    private WeddingSide getUserSideInWedding(Long weddingId, User currentUser) {
        if (currentUser == null) return WeddingSide.SHARED;
        return weddingMemberRepository.findByWeddingIdAndUserId(weddingId, currentUser.getId())
                .map(WeddingMember::getSide)
                .orElse(WeddingSide.SHARED);
    }
}
