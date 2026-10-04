package rw.ac.auca.dashboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rw.ac.auca.budget.BudgetService;
import rw.ac.auca.budget.dto.BudgetSummaryResponse;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.dashboard.dto.DashboardSummaryResponse;
import rw.ac.auca.guest.Guest;
import rw.ac.auca.guest.GuestCategory;
import rw.ac.auca.guest.GuestRepository;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.homeprep.HomePrepRepository;
import rw.ac.auca.homeprep.HomePreparation;
import rw.ac.auca.seating.SeatingService;
import rw.ac.auca.seating.dto.SeatingOverviewResponse;
import rw.ac.auca.task.Task;
import rw.ac.auca.task.TaskPriority;
import rw.ac.auca.task.TaskRepository;
import rw.ac.auca.task.TaskStatus;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.vendor.VendorService;
import rw.ac.auca.vendor.dto.VendorSummaryResponse;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @Mock
    private WeddingCeremonyRepository weddingCeremonyRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private HomePrepRepository homePrepRepository;

    @Mock
    private BudgetService budgetService;

    @Mock
    private SeatingService seatingService;

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private DashboardService dashboardService;

    private User currentUser;
    private Wedding wedding;
    private WeddingMember member;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("planner@example.com")
                .role(Role.ROLE_BRIDE)
                .build();
        currentUser.setId(1L);

        wedding = Wedding.builder()
                .title("Jane & John Grand Wedding")
                .targetBudget(new BigDecimal("15000000.00"))
                .build();
        wedding.setId(10L);

        member = WeddingMember.builder()
                .wedding(wedding)
                .user(currentUser)
                .side(WeddingSide.BRIDE_SIDE)
                .build();
        member.setId(100L);
    }

    @Test
    void getDashboardSummary_AggregatesAllMetricsCorrectly() {
        Task t1 = Task.builder().wedding(wedding).status(TaskStatus.COMPLETED).priority(TaskPriority.HIGH).build();
        Task t2 = Task.builder().wedding(wedding).status(TaskStatus.IN_PROGRESS).priority(TaskPriority.MEDIUM).build();

        BudgetSummaryResponse budgetSummary = BudgetSummaryResponse.builder()
                .weddingId(10L)
                .totalPlannedRwf(new BigDecimal("12000000.00"))
                .totalSpentRwf(new BigDecimal("10000000.00"))
                .totalContributionsRwf(new BigDecimal("2000000.00"))
                .build();

        Guest g1 = Guest.builder().wedding(wedding).status(RsvpStatus.CONFIRMED).side(WeddingSide.BRIDE_SIDE).category(GuestCategory.FAMILY).build();
        Guest g2 = Guest.builder().wedding(wedding).status(RsvpStatus.DECLINED).side(WeddingSide.SHARED).category(GuestCategory.FRIEND).build();

        SeatingOverviewResponse seatingSummary = SeatingOverviewResponse.builder()
                .totalTables(5)
                .totalCapacity(50)
                .totalOccupiedSeats(30)
                .totalSeatedGuestsCount(25)
                .totalUnassignedGuestsCount(10)
                .overflowConflictsCount(0)
                .build();

        VendorSummaryResponse vendorSummary = VendorSummaryResponse.builder()
                .totalVendors(4)
                .bookedVendorsCount(3)
                .totalVendorCostRwf(new BigDecimal("8000000.00"))
                .totalPaidCostRwf(new BigDecimal("5000000.00"))
                .build();

        HomePreparation hp1 = HomePreparation.builder().wedding(wedding).isCompleted(true).side(WeddingSide.BRIDE_SIDE).build();
        HomePreparation hp2 = HomePreparation.builder().wedding(wedding).isCompleted(false).side(WeddingSide.BRIDE_SIDE).build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(taskRepository.findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(eq(10L), anyList())).thenReturn(List.of(t1, t2));
        when(budgetService.getBudgetSummary(currentUser)).thenReturn(budgetSummary);
        when(guestRepository.findByWeddingIdAndSideInAndDeletedAtIsNull(eq(10L), anyList())).thenReturn(List.of(g1, g2));
        when(seatingService.getSeatingOverview(10L, currentUser)).thenReturn(seatingSummary);
        when(vendorService.getVendorsByWedding(10L, currentUser)).thenReturn(vendorSummary);
        when(homePrepRepository.findByWeddingIdAndSideIn(eq(10L), anyList())).thenReturn(List.of(hp1, hp2));
        when(weddingCeremonyRepository.findByWeddingId(10L)).thenReturn(List.of(WeddingCeremony.builder().build()));

        DashboardSummaryResponse summary = dashboardService.getDashboardSummary(10L, currentUser);

        assertNotNull(summary);
        assertEquals(10L, summary.getWeddingId());
        assertEquals("Jane & John Grand Wedding", summary.getWeddingTitle());
        assertEquals(2, summary.getTotalTasks());
        assertEquals(1, summary.getCompletedTasks());
        assertEquals(50.0, summary.getTaskCompletionPercentage());
        assertEquals(new BigDecimal("15000000.00"), summary.getTargetBudget());
        assertEquals(new BigDecimal("5000000.00"), summary.getRemainingBudget());
        assertEquals(2, summary.getTotalGuests());
        assertEquals(100.0, summary.getRsvpResponseRatePercentage());
        assertEquals(5, summary.getTotalTables());
        assertEquals(4, summary.getTotalVendors());
        assertEquals(3, summary.getBookedVendorsCount());
        assertEquals(2, summary.getTotalHomePrepItems());
        assertEquals(50.0, summary.getHomePrepCompletionPercentage());
        assertEquals(1, summary.getCeremoniesCount());
    }
}
