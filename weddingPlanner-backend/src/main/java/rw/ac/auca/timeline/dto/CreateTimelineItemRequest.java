package rw.ac.auca.timeline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTimelineItemRequest {

    @NotNull(message = "Wedding ID is required")
    private Long weddingId;

    private Long ceremonyId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Target date is required")
    private LocalDate targetDate;

    private LocalTime targetTime;

    private Boolean completed;
}
