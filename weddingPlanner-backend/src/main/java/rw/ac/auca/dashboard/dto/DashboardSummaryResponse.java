package rw.ac.auca.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private Long weddingId;
    private String weddingTitle;
    private String brideName;
    private String groomName;

    // Task Metrics
    private int totalTasks;
    private int completedTasks;
    private double taskCompletionPercentage;

    // Financial & Budget Metrics
    private BigDecimal targetBudget;
    private BigDecimal totalPlannedBudget;
    private BigDecimal totalActualExpenses;
    private BigDecimal totalMomoContributions;
    private BigDecimal remainingBudget;

    // Guest & RSVP Metrics
    private int totalGuests;
    private int confirmedGuests;
    private int attendingGuests;
    private int declinedGuests;
    private int pendingGuests;
    private double rsvpResponseRatePercentage;

    // Seating Metrics
    private int totalTables;
    private int totalSeatingCapacity;
    private int seatedGuestsCount;
    private int unassignedGuestsCount;
    private int seatingOverflowConflictsCount;

    // Vendor Metrics
    private int totalVendors;
    private int bookedVendorsCount;
    private BigDecimal totalVendorCostRwf;
    private BigDecimal totalPaidVendorCostRwf;

    // Household Prep Metrics
    private int totalHomePrepItems;
    private int preparedHomePrepItems;
    private double homePrepCompletionPercentage;

    // Ceremonies Count
    private int ceremoniesCount;
}
