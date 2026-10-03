package rw.ac.auca.homeprep;

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
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "home_preparations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePreparation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private Wedding wedding;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private HomePrepCategory category;

    @Column(name = "item_name", nullable = false, length = 200)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private WeddingSide side = WeddingSide.SHARED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Column(name = "budget_rwf", precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal budgetRwf = BigDecimal.ZERO;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private boolean isCompleted = false;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
