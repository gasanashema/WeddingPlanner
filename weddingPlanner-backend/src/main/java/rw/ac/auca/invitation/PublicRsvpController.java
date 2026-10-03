package rw.ac.auca.invitation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.common.ApiResponse;
import rw.ac.auca.invitation.dto.PublicInvitationDetailsResponse;
import rw.ac.auca.invitation.dto.SubmitRsvpRequest;

@RestController
@RequestMapping("/api/v1/public/invitations")
@RequiredArgsConstructor
public class PublicRsvpController {

    private final RsvpService rsvpService;

    @GetMapping("/{token}")
    public ResponseEntity<ApiResponse<PublicInvitationDetailsResponse>> getPublicInvitationDetails(
            @PathVariable String token) {
        try {
            PublicInvitationDetailsResponse details = rsvpService.getPublicInvitationDetails(token);
            return ResponseEntity.ok(ApiResponse.success(details));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{token}/rsvp")
    public ResponseEntity<ApiResponse<PublicInvitationDetailsResponse>> submitRsvp(
            @PathVariable String token,
            @Valid @RequestBody SubmitRsvpRequest request) {
        try {
            PublicInvitationDetailsResponse details = rsvpService.submitRsvp(token, request);
            return ResponseEntity.ok(ApiResponse.success("RSVP response recorded successfully", details));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
