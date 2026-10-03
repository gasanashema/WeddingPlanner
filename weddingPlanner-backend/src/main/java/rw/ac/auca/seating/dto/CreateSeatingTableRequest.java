package rw.ac.auca.seating.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSeatingTableRequest {

    @NotNull(message = "Wedding ID is required")
    private Long weddingId;

    private Long ceremonyId;

    @NotBlank(message = "Table name is required")
    private String tableName;

    @Min(value = 1, message = "Capacity must be at least 1 seat")
    private int capacity;
}
