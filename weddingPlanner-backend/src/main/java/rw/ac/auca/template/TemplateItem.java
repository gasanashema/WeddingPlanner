package rw.ac.auca.template;

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
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;

@Entity
@Table(name = "template_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private PlanningTemplate template;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "item_type", nullable = false, length = 50)
    private String itemType; // e.g. "APPLIANCES", "FURNITURE", "KITCHENWARE", "BEDDING", "RENT_UTILITIES" or "TASK"

    @Enumerated(EnumType.STRING)
    @Column(name = "default_side", nullable = false, length = 30)
    @Builder.Default
    private WeddingSide defaultSide = WeddingSide.SHARED;

    @Column(name = "estimated_budget", precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal estimatedBudget = BigDecimal.ZERO;
}
