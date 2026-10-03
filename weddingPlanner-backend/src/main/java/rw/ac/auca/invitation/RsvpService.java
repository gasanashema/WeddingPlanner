package rw.ac.auca.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.ceremony.dto.CeremonyResponse;
import rw.ac.auca.guest.Guest;
import rw.ac.auca.guest.GuestRepository;
import rw.ac.auca.invitation.dto.PublicInvitationDetailsResponse;
import rw.ac.auca.invitation.dto.SubmitRsvpRequest;
import rw.ac.auca.messaging.EventPublisher;
import rw.ac.auca.messaging.dto.RsvpNotificationEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RsvpService {

    private final InvitationRepository invitationRepository;
    private final RsvpRepository rsvpRepository;
    private final GuestRepository guestRepository;
    private final WeddingCeremonyRepository ceremonyRepository;
    private final EventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public PublicInvitationDetailsResponse getPublicInvitationDetails(String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation token: " + token));

        Guest guest = invitation.getGuest();
        List<WeddingCeremony> ceremonies = ceremonyRepository.findByWeddingId(invitation.getWedding().getId());
        List<CeremonyResponse> ceremonyResponses = ceremonies.stream().map(CeremonyResponse::fromEntity).toList();

        Rsvp rsvp = invitation.getRsvp();

        return PublicInvitationDetailsResponse.builder()
                .token(invitation.getToken())
                .weddingTitle(invitation.getWedding().getTitle())
                .guestName(guest.getFullName())
                .plusOneAllowed(guest.getPlusOneAllowed())
                .status(guest.getStatus())
                .personalMessage(invitation.getPersonalMessage())
                .plusOneNames(rsvp != null ? rsvp.getPlusOneNames() : null)
                .dietaryPreferences(rsvp != null ? rsvp.getDietaryPreferences() : null)
                .respondedAt(rsvp != null ? rsvp.getRespondedAt() : null)
                .ceremonies(ceremonyResponses)
                .build();
    }

    public PublicInvitationDetailsResponse submitRsvp(String token, SubmitRsvpRequest request) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation token: " + token));

        Guest guest = invitation.getGuest();

        // Validate plus-one count if provided
        if (request.getPlusOneNames() != null && !request.getPlusOneNames().isBlank()) {
            long submittedCount = Arrays.stream(request.getPlusOneNames().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .count();

            if (submittedCount > guest.getPlusOneAllowed()) {
                throw new IllegalArgumentException(
                        "Number of plus-ones (" + submittedCount + ") exceeds allowed limit of " + guest.getPlusOneAllowed()
                );
            }
        }

        guest.setStatus(request.getStatus());
        guestRepository.save(guest);

        Optional<Rsvp> existingRsvp = rsvpRepository.findByInvitationId(invitation.getId());

        Rsvp rsvp;
        if (existingRsvp.isPresent()) {
            rsvp = existingRsvp.get();
            rsvp.setStatus(request.getStatus());
            rsvp.setPlusOneNames(request.getPlusOneNames());
            rsvp.setDietaryPreferences(request.getDietaryPreferences());
            rsvp.setRespondedAt(Instant.now());
        } else {
            rsvp = Rsvp.builder()
                    .invitation(invitation)
                    .status(request.getStatus())
                    .plusOneNames(request.getPlusOneNames())
                    .dietaryPreferences(request.getDietaryPreferences())
                    .respondedAt(Instant.now())
                    .build();
        }

        Rsvp savedRsvp = rsvpRepository.save(rsvp);
        invitation.setRsvp(savedRsvp);

        // Publish RabbitMQ RSVP notification event
        eventPublisher.publishRsvpNotification(RsvpNotificationEvent.builder()
                .weddingId(invitation.getWedding().getId())
                .guestName(guest.getFullName())
                .status(guest.getStatus())
                .plusOneNames(request.getPlusOneNames())
                .build());

        return getPublicInvitationDetails(token);
    }
}
