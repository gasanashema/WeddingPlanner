package rw.ac.auca.wedding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateWeddingRequest {
    private String title;
    private BigDecimal targetBudget;
    private Long brideId;
    private Long groomId;
}
