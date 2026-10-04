package rw.ac.auca.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorSummaryResponse {

    private int totalVendors;
    private int bookedVendorsCount;
    private BigDecimal totalVendorCostRwf;
    private BigDecimal totalPaidCostRwf;
    private List<VendorResponse> vendors;
}
