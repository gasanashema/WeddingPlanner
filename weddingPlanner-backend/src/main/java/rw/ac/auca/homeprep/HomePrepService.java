package rw.ac.auca.homeprep;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.homeprep.dto.CreateHomePrepRequest;
import rw.ac.auca.homeprep.dto.HomePrepResponse;
import rw.ac.auca.homeprep.dto.UpdateHomePrepRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HomePrepService {

    private final HomePrepRepository homePrepRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;
    private final UserRepository userRepository;

    public HomePrepResponse createHomePrep(CreateHomePrepRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        WeddingSide itemSide = request.getSide() != null ? request.getSide() : userSide;
        if (itemSide == null) {
            itemSide = WeddingSide.SHARED;
        }

        if (!canUserAccessSide(userSide, itemSide)) {
            throw new AccessDeniedException("Forbidden: You cannot create a household preparation item for the opposite side.");
        }

        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId()).orElse(null);
        }

        HomePreparation prep = HomePreparation.builder()
                .wedding(wedding)
                .category(request.getCategory())
                .itemName(request.getItemName())
                .side(itemSide)
                .assignedUser(assignedUser)
                .budgetRwf(request.getBudgetRwf() != null ? request.getBudgetRwf() : BigDecimal.ZERO)
                .dueDate(request.getDueDate())
                .isCompleted(Boolean.TRUE.equals(request.getIsCompleted()))
                .notes(request.getNotes())
                .build();

        HomePreparation saved = homePrepRepository.save(prep);
        return HomePrepResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<HomePrepResponse> getHomePrepsForUser(User currentUser) {
        Wedding wedding = getUserActiveWedding(currentUser);
        if (wedding == null) {
            return List.of();
        }

        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);
        List<WeddingSide> allowedSides = getAllowedSides(userSide);

        return homePrepRepository.findByWeddingIdAndSideIn(wedding.getId(), allowedSides)
                .stream()
                .map(HomePrepResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public HomePrepResponse getHomePrepById(Long id, User currentUser) {
        HomePreparation prep = homePrepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home preparation item not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(prep.getWedding().getId(), currentUser);
        if (!canUserAccessSide(userSide, prep.getSide())) {
            throw new AccessDeniedException("Forbidden: Access denied to private household item of the opposite side.");
        }

        return HomePrepResponse.fromEntity(prep);
    }

    public HomePrepResponse updateHomePrep(Long id, UpdateHomePrepRequest request, User currentUser) {
        HomePreparation prep = homePrepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home preparation item not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(prep.getWedding().getId(), currentUser);
        if (!canUserAccessSide(userSide, prep.getSide())) {
            throw new AccessDeniedException("Forbidden: Access denied to private household item of the opposite side.");
        }

        if (request.getCategory() != null) {
            prep.setCategory(request.getCategory());
        }
        if (request.getItemName() != null) {
            prep.setItemName(request.getItemName());
        }
        if (request.getSide() != null) {
            if (!canUserAccessSide(userSide, request.getSide())) {
                throw new AccessDeniedException("Forbidden: Cannot change item side to the opposite side.");
            }
            prep.setSide(request.getSide());
        }
        if (request.getAssignedUserId() != null) {
            User assigned = userRepository.findById(request.getAssignedUserId()).orElse(null);
            prep.setAssignedUser(assigned);
        }
        if (request.getBudgetRwf() != null) {
            prep.setBudgetRwf(request.getBudgetRwf());
        }
        if (request.getDueDate() != null) {
            prep.setDueDate(request.getDueDate());
        }
        if (request.getIsCompleted() != null) {
            prep.setCompleted(request.getIsCompleted());
        }
        if (request.getNotes() != null) {
            prep.setNotes(request.getNotes());
        }

        HomePreparation updated = homePrepRepository.save(prep);
        return HomePrepResponse.fromEntity(updated);
    }

    public void deleteHomePrep(Long id, User currentUser) {
        HomePreparation prep = homePrepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home preparation item not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(prep.getWedding().getId(), currentUser);
        if (!canUserAccessSide(userSide, prep.getSide())) {
            throw new AccessDeniedException("Forbidden: Access denied to private household item of the opposite side.");
        }

        homePrepRepository.delete(prep);
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

    private List<WeddingSide> getAllowedSides(WeddingSide userSide) {
        if (userSide == WeddingSide.BRIDE_SIDE) {
            return List.of(WeddingSide.BRIDE_SIDE, WeddingSide.SHARED);
        } else if (userSide == WeddingSide.GROOM_SIDE) {
            return List.of(WeddingSide.GROOM_SIDE, WeddingSide.SHARED);
        }
        return List.of(WeddingSide.SHARED);
    }

    private boolean canUserAccessSide(WeddingSide userSide, WeddingSide itemSide) {
        if (itemSide == WeddingSide.SHARED) return true;
        if (userSide == WeddingSide.BRIDE_SIDE && itemSide == WeddingSide.BRIDE_SIDE) return true;
        if (userSide == WeddingSide.GROOM_SIDE && itemSide == WeddingSide.GROOM_SIDE) return true;
        return false;
    }
}
