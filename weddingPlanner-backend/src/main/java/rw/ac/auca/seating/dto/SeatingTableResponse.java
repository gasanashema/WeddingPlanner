package rw.ac.auca.seating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatingTableResponse {

    private Long id;
    private Long weddingId;
    private Long ceremonyId;
    private String ceremonyName;
    private String tableName;
    private int capacity;
    private int occupiedSeats;
    private boolean isOverflowing;
    private List<GuestSeatingResponse> seatedGuests;
    private Instant createdAt;
    private Instant updatedAt;
}
