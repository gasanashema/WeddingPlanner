package rw.ac.auca.budget.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.task.VisibilityScope;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExpenseRequest {

    private Long ceremonyId;
    private Long budgetCategoryId;

    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @DecimalMin(value = "0.01", message = "Expense amount must be positive and greater than zero")
    private BigDecimal amountRwf;

    private Long paidByUserId;
    private LocalDate dateSpent;
    private VisibilityScope visibilityScope;
    private String receiptNotes;
}
