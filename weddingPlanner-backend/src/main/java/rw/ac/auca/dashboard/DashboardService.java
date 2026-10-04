package rw.ac.auca.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.budget.BudgetService;
import rw.ac.auca.budget.dto.BudgetSummaryResponse;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.dashboard.dto.DashboardSummaryResponse;
import rw.ac.auca.guest.Guest;
import rw.ac.auca.guest.GuestRepository;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.homeprep.HomePrepRepository;
import rw.ac.auca.homeprep.HomePreparation;
import rw.ac.auca.seating.SeatingService;
import rw.ac.auca.seating.dto.SeatingOverviewResponse;
import rw.ac.auca.task.Task;
import rw.ac.auca.task.TaskRepository;
import rw.ac.auca.task.TaskStatus;
import rw.ac.auca.task.VisibilityScope;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;
    private final WeddingCeremonyRepository weddingCeremonyRepository;
    private final TaskRepository taskRepository;
    private final GuestRepository guestRepository;
    private final HomePrepRepository homePrepRepository;
    private final BudgetService budgetService;
    private final SeatingService seatingService;
    private final VendorService vendorService;

    public DashboardSummaryResponse getDashboardSummary(Long requestedWeddingId, User currentUser) {
        Wedding wedding = resolveWeddingForUser(requestedWeddingId, currentUser);
        validateUserMembership(wedding.getId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        // 1. Task Metrics
        List<VisibilityScope> allowedScopes = getAllowedTaskScopes(userSide);
        List<Task> tasks = taskRepository.findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(wedding.getId(), allowedScopes);
        int totalTasks = tasks.size();
        int completedTasks = (int) tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        double taskCompletionRate = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0.0;

        // 2. Budget Metrics
        BudgetSummaryResponse budgetSummary = budgetService.getBudgetSummary(currentUser);
        BigDecimal targetBudget = wedding.getTargetBudget() != null ? wedding.getTargetBudget() : BigDecimal.ZERO;
        BigDecimal plannedBudget = budgetSummary.getTotalPlannedRwf() != null ? budgetSummary.getTotalPlannedRwf() : BigDecimal.ZERO;
        BigDecimal actualExpenses = budgetSummary.getTotalSpentRwf() != null ? budgetSummary.getTotalSpentRwf() : BigDecimal.ZERO;
        BigDecimal momoContributions = budgetSummary.getTotalContributionsRwf() != null ? budgetSummary.getTotalContributionsRwf() : BigDecimal.ZERO;
        BigDecimal remainingBudget = targetBudget.subtract(actualExpenses);

        // 3. Guest & RSVP Metrics
        List<WeddingSide> allowedGuestSides = getAllowedSides(userSide);
        List<Guest> guests = guestRepository.findByWeddingIdAndSideInAndDeletedAtIsNull(wedding.getId(), allowedGuestSides);
        int totalGuests = guests.size();
        int confirmedGuests = (int) guests.stream().filter(g -> g.getStatus() == RsvpStatus.CONFIRMED).count();
        int attendingGuests = (int) guests.stream().filter(g -> g.getStatus() == RsvpStatus.ATTENDING).count();
        int declinedGuests = (int) guests.stream().filter(g -> g.getStatus() == RsvpStatus.DECLINED).count();
        int pendingGuests = (int) guests.stream().filter(g -> g.getStatus() == RsvpStatus.PENDING).count();
        int totalResponded = confirmedGuests + attendingGuests + declinedGuests;
        double rsvpRate = totalGuests > 0 ? (totalResponded * 100.0 / totalGuests) : 0.0;

        // 4. Seating Metrics
        SeatingOverviewResponse seatingOverview = seatingService.getSeatingOverview(wedding.getId(), currentUser);

        // 5. Vendor Metrics
        VendorSummaryResponse vendorSummary = vendorService.getVendorsByWedding(wedding.getId(), currentUser);

        // 6. Home Prep Metrics
        List<HomePreparation> homePrepItems = homePrepRepository.findByWeddingIdAndSideIn(wedding.getId(), allowedGuestSides);
        int totalHomePrep = homePrepItems.size();
        int preparedHomePrep = (int) homePrepItems.stream().filter(HomePreparation::isCompleted).count();
        double homePrepCompletionRate = totalHomePrep > 0 ? (preparedHomePrep * 100.0 / totalHomePrep) : 0.0;

        // 7. Ceremonies Count
        int ceremoniesCount = weddingCeremonyRepository.findByWeddingId(wedding.getId()).size();

        return DashboardSummaryResponse.builder()
                .weddingId(wedding.getId())
                .weddingTitle(wedding.getTitle())
                .brideName(getUserFullName(wedding.getBride()))
                .groomName(getUserFullName(wedding.getGroom()))
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .taskCompletionPercentage(Math.round(taskCompletionRate * 100.0) / 100.0)
                .targetBudget(targetBudget)
                .totalPlannedBudget(plannedBudget)
                .totalActualExpenses(actualExpenses)
                .totalMomoContributions(momoContributions)
                .remainingBudget(remainingBudget)
                .totalGuests(totalGuests)
                .confirmedGuests(confirmedGuests)
                .attendingGuests(attendingGuests)
                .declinedGuests(declinedGuests)
                .pendingGuests(pendingGuests)
                .rsvpResponseRatePercentage(Math.round(rsvpRate * 100.0) / 100.0)
                .totalTables(seatingOverview.getTotalTables())
                .totalSeatingCapacity(seatingOverview.getTotalCapacity())
                .seatedGuestsCount(seatingOverview.getTotalSeatedGuestsCount())
                .unassignedGuestsCount(seatingOverview.getTotalUnassignedGuestsCount())
                .seatingOverflowConflictsCount(seatingOverview.getOverflowConflictsCount())
                .totalVendors(vendorSummary.getTotalVendors())
                .bookedVendorsCount(vendorSummary.getBookedVendorsCount())
                .totalVendorCostRwf(vendorSummary.getTotalVendorCostRwf())
                .totalPaidVendorCostRwf(vendorSummary.getTotalPaidCostRwf())
                .totalHomePrepItems(totalHomePrep)
                .preparedHomePrepItems(preparedHomePrep)
                .homePrepCompletionPercentage(Math.round(homePrepCompletionRate * 100.0) / 100.0)
                .ceremoniesCount(ceremoniesCount)
                .build();
    }

    private String getUserFullName(User user) {
        if (user == null) return null;
        String fn = user.getFirstName() != null ? user.getFirstName() : "";
        String ln = user.getLastName() != null ? user.getLastName() : "";
        return (fn + " " + ln).trim();
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

    private WeddingSide getUserSideInWedding(Long weddingId, User currentUser) {
        if (currentUser == null) return WeddingSide.SHARED;
        return weddingMemberRepository.findByWeddingIdAndUserId(weddingId, currentUser.getId())
                .map(WeddingMember::getSide)
                .orElse(WeddingSide.SHARED);
    }

    private List<VisibilityScope> getAllowedTaskScopes(WeddingSide userSide) {
        if (userSide == WeddingSide.BRIDE_SIDE) {
            return List.of(VisibilityScope.BRIDE_PRIVATE, VisibilityScope.SHARED);
        } else if (userSide == WeddingSide.GROOM_SIDE) {
            return List.of(VisibilityScope.GROOM_PRIVATE, VisibilityScope.SHARED);
        }
        return List.of(VisibilityScope.BRIDE_PRIVATE, VisibilityScope.GROOM_PRIVATE, VisibilityScope.SHARED);
    }

    private List<WeddingSide> getAllowedSides(WeddingSide userSide) {
        if (userSide == WeddingSide.BRIDE_SIDE) {
            return List.of(WeddingSide.BRIDE_SIDE, WeddingSide.SHARED);
        } else if (userSide == WeddingSide.GROOM_SIDE) {
            return List.of(WeddingSide.GROOM_SIDE, WeddingSide.SHARED);
        }
        return List.of(WeddingSide.BRIDE_SIDE, WeddingSide.GROOM_SIDE, WeddingSide.SHARED);
    }
}
