package rw.ac.auca.timeline.dto;

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
public class TimelineItemResponse {

    private Long id;
    private Long weddingId;
    private Long ceremonyId;
    private String ceremonyName;
    private String title;
    private LocalDate targetDate;
    private LocalTime targetTime;
    private boolean completed;
}
