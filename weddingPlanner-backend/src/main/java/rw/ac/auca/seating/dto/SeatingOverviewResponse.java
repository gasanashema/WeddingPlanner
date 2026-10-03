package rw.ac.auca.seating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatingOverviewResponse {

    private int totalTables;
    private int totalCapacity;
    private int totalOccupiedSeats;
    private int totalSeatedGuestsCount;
    private int totalUnassignedGuestsCount;
    private int overflowConflictsCount;
    private List<SeatingTableResponse> tables;
    private List<UnassignedGuestResponse> unassignedGuests;
}
