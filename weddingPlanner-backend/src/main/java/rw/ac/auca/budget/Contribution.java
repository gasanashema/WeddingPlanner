package rw.ac.auca.budget;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.ac.auca.wedding.Wedding;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contributions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private Wedding wedding;

    @Column(name = "contributor_name", nullable = false, length = 120)
    private String contributorName;

    @Column(name = "contributor_phone", nullable = false, length = 30)
    private String contributorPhone;

    @Column(name = "amount_rwf", nullable = false, precision = 14, scale = 2)
    private BigDecimal amountRwf;

    @Column(name = "momo_transaction_id", unique = true, length = 100)
    private String momoTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private ContributionStatus status = ContributionStatus.PENDING;

    @Column(name = "payment_date")
    @Builder.Default
    private LocalDateTime paymentDate = LocalDateTime.now();
}
