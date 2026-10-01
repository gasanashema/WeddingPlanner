package rw.ac.auca.wedding;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.math.BigDecimal;

@Entity
@Table(name = "weddings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wedding extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "target_budget", precision = 15, scale = 2)
    private BigDecimal targetBudget;

    @Column(name = "partner_code", unique = true, length = 36)
    private String partnerCode;

    @Column(name = "family_code", unique = true, length = 36)
    private String familyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bride_id")
    private User bride;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groom_id")
    private User groom;
}
