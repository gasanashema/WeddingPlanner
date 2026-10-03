package rw.ac.auca.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.budget.BudgetCategory;
import rw.ac.auca.task.VisibilityScope;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCategoryResponse {

    private Long id;
    private String name;
    private BigDecimal allocatedAmountRwf;
    private BigDecimal spentAmountRwf;
    private VisibilityScope visibilityScope;

    public static BudgetCategoryResponse fromEntity(BudgetCategory entity, BigDecimal spentAmount) {
        if (entity == null) return null;
        return BudgetCategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .allocatedAmountRwf(entity.getAllocatedAmountRwf())
                .spentAmountRwf(spentAmount != null ? spentAmount : BigDecimal.ZERO)
                .visibilityScope(entity.getVisibilityScope())
                .build();
    }
}
