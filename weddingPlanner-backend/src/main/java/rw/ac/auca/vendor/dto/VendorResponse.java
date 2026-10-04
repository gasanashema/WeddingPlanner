package rw.ac.auca.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.vendor.BookingStatus;
import rw.ac.auca.vendor.VendorCategory;
import rw.ac.auca.vendor.VendorPaymentStatus;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponse {

    private Long id;
    private Long weddingId;
    private String name;
    private VendorCategory category;
    private String contactPhone;
    private String contactEmail;
    private BigDecimal costRwf;
    private BookingStatus bookingStatus;
    private VendorPaymentStatus paymentStatus;
    private String notes;
}
