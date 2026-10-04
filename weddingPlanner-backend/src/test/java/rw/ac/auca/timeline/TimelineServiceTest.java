package rw.ac.auca.timeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.timeline.dto.CreateTimelineItemRequest;
import rw.ac.auca.timeline.dto.TimelineItemResponse;
import rw.ac.auca.timeline.dto.UpdateTimelineItemRequest;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimelineServiceTest {

    @Mock
    private TimelineItemRepository timelineItemRepository;

    @Mock
    private WeddingCeremonyRepository weddingCeremonyRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @InjectMocks
    private TimelineService timelineService;

    private User currentUser;
    private Wedding wedding;
    private WeddingMember member;
    private WeddingCeremony ceremony;
    private TimelineItem item;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .email("planner@example.com")
                .role(Role.ROLE_BRIDE)
                .build();
        currentUser.setId(1L);

        wedding = Wedding.builder()
                .title("Alice & Bob Wedding")
                .build();
        wedding.setId(10L);

        member = WeddingMember.builder()
                .wedding(wedding)
                .user(currentUser)
                .side(WeddingSide.BRIDE_SIDE)
                .build();
        member.setId(100L);

        ceremony = WeddingCeremony.builder()
                .wedding(wedding)
                .name("Church Service")
                .build();
        ceremony.setId(20L);

        item = TimelineItem.builder()
                .wedding(wedding)
                .ceremony(ceremony)
                .title("Guest Arrival & Processional")
                .targetDate(LocalDate.of(2026, 12, 20))
                .targetTime(LocalTime.of(10, 0))
                .completed(false)
                .build();
        item.setId(600L);
    }

    @Test
    void createTimelineItem_Success() {
        CreateTimelineItemRequest req = CreateTimelineItemRequest.builder()
                .weddingId(10L)
                .ceremonyId(20L)
                .title("Guest Arrival & Processional")
                .targetDate(LocalDate.of(2026, 12, 20))
                .targetTime(LocalTime.of(10, 0))
                .completed(false)
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(weddingCeremonyRepository.findByIdAndWeddingId(20L, 10L)).thenReturn(Optional.of(ceremony));
        when(timelineItemRepository.save(any(TimelineItem.class))).thenAnswer(i -> {
            TimelineItem t = i.getArgument(0);
            t.setId(601L);
            return t;
        });

        TimelineItemResponse response = timelineService.createTimelineItem(req, currentUser);

        assertNotNull(response);
        assertEquals(601L, response.getId());
        assertEquals("Guest Arrival & Processional", response.getTitle());
        assertEquals(20L, response.getCeremonyId());
        assertEquals("Church Service", response.getCeremonyName());
        assertFalse(response.isCompleted());
    }

    @Test
    void updateTimelineItem_Success() {
        UpdateTimelineItemRequest req = UpdateTimelineItemRequest.builder()
                .title("Updated Processional Start")
                .targetDate(LocalDate.of(2026, 12, 20))
                .targetTime(LocalTime.of(10, 30))
                .completed(true)
                .build();

        when(timelineItemRepository.findById(600L)).thenReturn(Optional.of(item));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(timelineItemRepository.save(any(TimelineItem.class))).thenReturn(item);

        TimelineItemResponse response = timelineService.updateTimelineItem(600L, req, currentUser);

        assertNotNull(response);
        assertEquals("Updated Processional Start", response.getTitle());
        assertEquals(LocalTime.of(10, 30), response.getTargetTime());
        assertTrue(response.isCompleted());
    }

    @Test
    void toggleTimelineItemCompletion_Success() {
        when(timelineItemRepository.findById(600L)).thenReturn(Optional.of(item));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(timelineItemRepository.save(any(TimelineItem.class))).thenReturn(item);

        TimelineItemResponse response = timelineService.toggleTimelineItemCompletion(600L, currentUser);

        assertNotNull(response);
        assertTrue(response.isCompleted());
    }

    @Test
    void deleteTimelineItem_Success() {
        when(timelineItemRepository.findById(600L)).thenReturn(Optional.of(item));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));

        timelineService.deleteTimelineItem(600L, currentUser);

        verify(timelineItemRepository, times(1)).delete(item);
    }

    @Test
    void getTimelineItems_SortedByDateAndTime() {
        TimelineItem item2 = TimelineItem.builder()
                .wedding(wedding)
                .title("Reception Toast & Cake Cutting")
                .targetDate(LocalDate.of(2026, 12, 20))
                .targetTime(LocalTime.of(16, 0))
                .completed(false)
                .build();
        item2.setId(602L);

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(timelineItemRepository.findByWeddingIdOrderByTargetDateAscTargetTimeAsc(10L)).thenReturn(List.of(item, item2));

        List<TimelineItemResponse> items = timelineService.getTimelineItems(10L, null, currentUser);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Guest Arrival & Processional", items.get(0).getTitle());
        assertEquals("Reception Toast & Cake Cutting", items.get(1).getTitle());
    }
}
