package rw.ac.auca.invitation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.guest.RsvpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitRsvpRequest {

    @NotNull(message = "RSVP status is required")
    private RsvpStatus status;

    private String plusOneNames;
    private String dietaryPreferences;
}
