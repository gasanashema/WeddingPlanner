package rw.ac.auca.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.budget.Expense;
import rw.ac.auca.task.VisibilityScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {

    private Long id;
    private Long weddingId;
    private Long ceremonyId;
    private String ceremonyName;
    private Long budgetCategoryId;
    private String budgetCategoryName;
    private String title;
    private BigDecimal amountRwf;
    private Long paidByUserId;
    private String paidByUserName;
    private LocalDate dateSpent;
    private VisibilityScope visibilityScope;
    private String receiptNotes;
    private LocalDateTime deletedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public static ExpenseResponse fromEntity(Expense entity) {
        if (entity == null) return null;

        String ceremonyName = entity.getCeremony() != null ? entity.getCeremony().getName() : null;
        String categoryName = entity.getBudgetCategory() != null ? entity.getBudgetCategory().getName() : null;
        String paidByName = null;
        if (entity.getPaidByUser() != null) {
            paidByName = entity.getPaidByUser().getFirstName() + " " + entity.getPaidByUser().getLastName();
        }

        return ExpenseResponse.builder()
                .id(entity.getId())
                .weddingId(entity.getWedding() != null ? entity.getWedding().getId() : null)
                .ceremonyId(entity.getCeremony() != null ? entity.getCeremony().getId() : null)
                .ceremonyName(ceremonyName)
                .budgetCategoryId(entity.getBudgetCategory() != null ? entity.getBudgetCategory().getId() : null)
                .budgetCategoryName(categoryName)
                .title(entity.getTitle())
                .amountRwf(entity.getAmountRwf())
                .paidByUserId(entity.getPaidByUser() != null ? entity.getPaidByUser().getId() : null)
                .paidByUserName(paidByName)
                .dateSpent(entity.getDateSpent())
                .visibilityScope(entity.getVisibilityScope())
                .receiptNotes(entity.getReceiptNotes())
                .deletedAt(entity.getDeletedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
