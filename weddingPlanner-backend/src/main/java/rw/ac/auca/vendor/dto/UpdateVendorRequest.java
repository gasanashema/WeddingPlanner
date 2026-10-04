package rw.ac.auca.vendor.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UpdateVendorRequest {

    @NotBlank(message = "Vendor name is required")
    private String name;

    @NotNull(message = "Vendor category is required")
    private VendorCategory category;

    private String contactPhone;

    private String contactEmail;

    @DecimalMin(value = "0.0", message = "Cost must be a non-negative amount")
    private BigDecimal costRwf;

    private BookingStatus bookingStatus;

    private VendorPaymentStatus paymentStatus;

    private String notes;
}
