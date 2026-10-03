package rw.ac.auca.seating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.guest.GuestCategory;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.wedding.WeddingSide;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnassignedGuestResponse {

    private Long guestId;
    private String fullName;
    private String phone;
    private String email;
    private WeddingSide side;
    private GuestCategory category;
    private RsvpStatus status;
    private int plusOneAllowed;
    private int requiredSeats;
}
