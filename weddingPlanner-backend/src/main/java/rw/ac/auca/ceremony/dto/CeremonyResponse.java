package rw.ac.auca.ceremony.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.ceremony.CeremonyType;
import rw.ac.auca.ceremony.WeddingCeremony;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CeremonyResponse {
    private Long id;
    private Long weddingId;
    private String weddingTitle;
    private CeremonyType ceremonyType;
    private String name;
    private LocalDate ceremonyDate;
    private LocalTime startTime;
    private String venueLocation;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public static CeremonyResponse fromEntity(WeddingCeremony ceremony) {
        return CeremonyResponse.builder()
                .id(ceremony.getId())
                .weddingId(ceremony.getWedding() != null ? ceremony.getWedding().getId() : null)
                .weddingTitle(ceremony.getWedding() != null ? ceremony.getWedding().getTitle() : null)
                .ceremonyType(ceremony.getCeremonyType())
                .name(ceremony.getName())
                .ceremonyDate(ceremony.getCeremonyDate())
                .startTime(ceremony.getStartTime())
                .venueLocation(ceremony.getVenueLocation())
                .description(ceremony.getDescription())
                .createdAt(ceremony.getCreatedAt())
                .updatedAt(ceremony.getUpdatedAt())
                .build();
    }
}
