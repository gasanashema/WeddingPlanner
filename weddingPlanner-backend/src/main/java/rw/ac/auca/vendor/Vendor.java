package rw.ac.auca.vendor;

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
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.ac.auca.wedding.Wedding;

import java.math.BigDecimal;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private Wedding wedding;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private VendorCategory category;

    @Column(name = "contact_phone", length = 30)
    private String contactPhone;

    @Column(name = "contact_email", length = 150)
    private String contactEmail;

    @Column(name = "cost_rwf", precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal costRwf = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false, length = 40)
    @Builder.Default
    private BookingStatus bookingStatus = BookingStatus.INQUIRY;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 40)
    @Builder.Default
    private VendorPaymentStatus paymentStatus = VendorPaymentStatus.UNPAID;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Version
    @Builder.Default
    private Long version = 0L;
}
