package rw.ac.auca.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminWeddingResponse {

    private Long id;
    private String title;
    private String partnerCode;
    private String familyCode;
    private BigDecimal targetBudget;
    private String brideName;
    private String groomName;
    private int totalMembers;
    private Instant createdAt;
}
