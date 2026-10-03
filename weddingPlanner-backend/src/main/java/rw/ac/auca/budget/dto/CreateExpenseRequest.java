package rw.ac.auca.budget.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateExpenseRequest {

    private Long weddingId;
    private Long ceremonyId;
    private Long budgetCategoryId;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Expense amount must be positive and greater than zero")
    private BigDecimal amountRwf;

    private Long paidByUserId;

    @NotNull(message = "Date spent is required")
    private LocalDate dateSpent;

    private VisibilityScope visibilityScope;

    private String receiptNotes;
}
