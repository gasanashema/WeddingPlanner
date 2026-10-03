package rw.ac.auca.seating.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatingTableRequest {

    private Long ceremonyId;

    @NotBlank(message = "Table name is required")
    private String tableName;

    @Min(value = 1, message = "Capacity must be at least 1 seat")
    private int capacity;
}
