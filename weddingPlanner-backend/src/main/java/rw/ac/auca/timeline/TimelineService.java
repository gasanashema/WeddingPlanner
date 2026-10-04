package rw.ac.auca.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.timeline.dto.CreateTimelineItemRequest;
import rw.ac.auca.timeline.dto.TimelineItemResponse;
import rw.ac.auca.timeline.dto.UpdateTimelineItemRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TimelineService {

    private final TimelineItemRepository timelineItemRepository;
    private final WeddingCeremonyRepository weddingCeremonyRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;

    public TimelineItemResponse createTimelineItem(CreateTimelineItemRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        validateUserMembership(wedding.getId(), currentUser);

        WeddingCeremony ceremony = null;
        if (request.getCeremonyId() != null) {
            ceremony = weddingCeremonyRepository.findByIdAndWeddingId(request.getCeremonyId(), wedding.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ceremony not found with id: " + request.getCeremonyId()));
        }

        TimelineItem item = TimelineItem.builder()
                .wedding(wedding)
                .ceremony(ceremony)
                .title(request.getTitle())
                .targetDate(request.getTargetDate())
                .targetTime(request.getTargetTime())
                .completed(Boolean.TRUE.equals(request.getCompleted()))
                .build();

        TimelineItem saved = timelineItemRepository.save(item);
        return mapToResponse(saved);
    }

    public TimelineItemResponse updateTimelineItem(Long itemId, UpdateTimelineItemRequest request, User currentUser) {
        TimelineItem item = timelineItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Timeline item not found with id: " + itemId));

        validateUserMembership(item.getWedding().getId(), currentUser);

        WeddingCeremony ceremony = null;
        if (request.getCeremonyId() != null) {
            ceremony = weddingCeremonyRepository.findByIdAndWeddingId(request.getCeremonyId(), item.getWedding().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ceremony not found with id: " + request.getCeremonyId()));
        }

        item.setCeremony(ceremony);
        item.setTitle(request.getTitle());
        item.setTargetDate(request.getTargetDate());
        item.setTargetTime(request.getTargetTime());
        if (request.getCompleted() != null) {
            item.setCompleted(request.getCompleted());
        }

        TimelineItem updated = timelineItemRepository.save(item);
        return mapToResponse(updated);
    }

    public TimelineItemResponse toggleTimelineItemCompletion(Long itemId, User currentUser) {
        TimelineItem item = timelineItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Timeline item not found with id: " + itemId));

        validateUserMembership(item.getWedding().getId(), currentUser);
        item.setCompleted(!item.isCompleted());

        TimelineItem updated = timelineItemRepository.save(item);
        return mapToResponse(updated);
    }

    public void deleteTimelineItem(Long itemId, User currentUser) {
        TimelineItem item = timelineItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Timeline item not found with id: " + itemId));

        validateUserMembership(item.getWedding().getId(), currentUser);
        timelineItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public List<TimelineItemResponse> getTimelineItems(Long requestedWeddingId, Long ceremonyId, User currentUser) {
        Wedding wedding = resolveWeddingForUser(requestedWeddingId, currentUser);
        validateUserMembership(wedding.getId(), currentUser);

        List<TimelineItem> items;
        if (ceremonyId != null) {
            items = timelineItemRepository.findByWeddingIdAndCeremonyIdOrderByTargetDateAscTargetTimeAsc(wedding.getId(), ceremonyId);
        } else {
            items = timelineItemRepository.findByWeddingIdOrderByTargetDateAscTargetTimeAsc(wedding.getId());
        }

        return items.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TimelineItemResponse mapToResponse(TimelineItem item) {
        return TimelineItemResponse.builder()
                .id(item.getId())
                .weddingId(item.getWedding().getId())
                .ceremonyId(item.getCeremony() != null ? item.getCeremony().getId() : null)
                .ceremonyName(item.getCeremony() != null ? item.getCeremony().getName() : null)
                .title(item.getTitle())
                .targetDate(item.getTargetDate())
                .targetTime(item.getTargetTime())
                .completed(item.isCompleted())
                .build();
    }

    private Wedding resolveWeddingForUser(Long requestedWeddingId, User currentUser) {
        if (requestedWeddingId != null) {
            return weddingRepository.findById(requestedWeddingId)
                    .orElseThrow(() -> new IllegalArgumentException("Wedding not found with id: " + requestedWeddingId));
        }
        List<WeddingMember> memberships = weddingMemberRepository.findByUserId(currentUser.getId());
        if (memberships.isEmpty()) {
            throw new IllegalArgumentException("User has no active wedding workspace.");
        }
        return memberships.get(0).getWedding();
    }

    private void validateUserMembership(Long weddingId, User currentUser) {
        if (currentUser == null) return;
        boolean isMember = weddingMemberRepository.findByWeddingIdAndUserId(weddingId, currentUser.getId()).isPresent();
        if (!isMember) {
            throw new AccessDeniedException("Forbidden: You are not a member of this wedding.");
        }
    }
}
