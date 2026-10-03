package rw.ac.auca.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSummaryResponse {

    private Long weddingId;
    private BigDecimal totalPlannedRwf;
    private BigDecimal totalSpentRwf;
    private BigDecimal remainingRwf;
    private BigDecimal totalContributionsRwf;
    private List<BudgetCategoryResponse> categories;
    private List<ExpenseResponse> expenses;
}
