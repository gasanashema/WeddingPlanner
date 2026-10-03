package rw.ac.auca.homeprep.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.homeprep.HomePrepCategory;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHomePrepRequest {

    private HomePrepCategory category;

    @Size(max = 200, message = "Item name cannot exceed 200 characters")
    private String itemName;

    private WeddingSide side;

    private Long assignedUserId;

    private BigDecimal budgetRwf;

    private LocalDate dueDate;

    private Boolean isCompleted;

    private String notes;
}
