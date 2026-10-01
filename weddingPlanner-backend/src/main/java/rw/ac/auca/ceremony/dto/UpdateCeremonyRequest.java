package rw.ac.auca.ceremony.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.ceremony.CeremonyType;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCeremonyRequest {
    private CeremonyType ceremonyType;
    private String name;
    private LocalDate ceremonyDate;
    private LocalTime startTime;
    private String venueLocation;
    private String description;
}
