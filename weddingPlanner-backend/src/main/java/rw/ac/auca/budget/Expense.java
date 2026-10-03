package rw.ac.auca.budget;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.ac.auca.base.BaseEntity;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.task.VisibilityScope;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private Wedding wedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ceremony_id")
    private WeddingCeremony ceremony;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_category_id")
    private BudgetCategory budgetCategory;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "amount_rwf", nullable = false, precision = 14, scale = 2)
    private BigDecimal amountRwf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_user_id")
    private User paidByUser;

    @Column(name = "date_spent", nullable = false)
    private LocalDate dateSpent;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_scope", nullable = false, length = 30)
    @Builder.Default
    private VisibilityScope visibilityScope = VisibilityScope.SHARED;

    @Column(name = "receipt_notes", columnDefinition = "TEXT")
    private String receiptNotes;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
