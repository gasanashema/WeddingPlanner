package rw.ac.auca.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.budget.Contribution;
import rw.ac.auca.budget.ContributionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionResponse {

    private Long id;
    private Long weddingId;
    private String contributorName;
    private String contributorPhone;
    private BigDecimal amountRwf;
    private String momoTransactionId;
    private ContributionStatus status;
    private LocalDateTime paymentDate;

    public static ContributionResponse fromEntity(Contribution entity) {
        if (entity == null) return null;
        return ContributionResponse.builder()
                .id(entity.getId())
                .weddingId(entity.getWedding() != null ? entity.getWedding().getId() : null)
                .contributorName(entity.getContributorName())
                .contributorPhone(entity.getContributorPhone())
                .amountRwf(entity.getAmountRwf())
                .momoTransactionId(entity.getMomoTransactionId())
                .status(entity.getStatus())
                .paymentDate(entity.getPaymentDate())
                .build();
    }
}
