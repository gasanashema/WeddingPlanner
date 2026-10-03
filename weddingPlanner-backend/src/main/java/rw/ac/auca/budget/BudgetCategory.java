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
import rw.ac.auca.task.VisibilityScope;

import java.math.BigDecimal;

@Entity
@Table(name = "budget_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "allocated_amount_rwf", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal allocatedAmountRwf = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_scope", nullable = false, length = 30)
    @Builder.Default
    private VisibilityScope visibilityScope = VisibilityScope.SHARED;
}
