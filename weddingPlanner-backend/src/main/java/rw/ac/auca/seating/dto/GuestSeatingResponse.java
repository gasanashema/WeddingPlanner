package rw.ac.auca.seating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.guest.GuestCategory;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.wedding.WeddingSide;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestSeatingResponse {

    private Long seatingId;
    private Long tableId;
    private String tableName;
    private Long guestId;
    private String guestName;
    private String guestPhone;
    private WeddingSide side;
    private GuestCategory category;
    private RsvpStatus status;
    private int plusOneAllowed;
    private int totalSeatsOccupied;
    private Instant assignedAt;
}
