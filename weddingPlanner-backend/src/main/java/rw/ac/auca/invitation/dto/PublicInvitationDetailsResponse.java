package rw.ac.auca.invitation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.ceremony.dto.CeremonyResponse;
import rw.ac.auca.guest.RsvpStatus;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicInvitationDetailsResponse {

    private String token;
    private String weddingTitle;
    private String guestName;
    private int plusOneAllowed;
    private RsvpStatus status;
    private String personalMessage;
    private String plusOneNames;
    private String dietaryPreferences;
    private Instant respondedAt;
    private List<CeremonyResponse> ceremonies;
}
