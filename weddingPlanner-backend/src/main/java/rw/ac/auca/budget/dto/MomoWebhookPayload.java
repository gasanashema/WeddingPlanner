package rw.ac.auca.budget.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.budget.ContributionStatus;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MomoWebhookPayload {

    @NotNull(message = "Wedding ID is required")
    private Long weddingId;

    @NotBlank(message = "Contributor name is required")
    private String contributorName;

    @NotBlank(message = "Contributor phone is required")
    private String contributorPhone;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Contribution amount must be positive")
    private BigDecimal amountRwf;

    @NotBlank(message = "MoMo transaction ID is required")
    private String momoTransactionId;

    private ContributionStatus status;

    private String signature;
}
