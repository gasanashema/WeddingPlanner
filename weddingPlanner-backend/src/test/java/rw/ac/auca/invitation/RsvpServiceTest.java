package rw.ac.auca.invitation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.guest.Guest;
import rw.ac.auca.guest.GuestCategory;
import rw.ac.auca.guest.GuestRepository;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.invitation.dto.PublicInvitationDetailsResponse;
import rw.ac.auca.invitation.dto.SubmitRsvpRequest;
import rw.ac.auca.messaging.EventPublisher;
import rw.ac.auca.messaging.dto.RsvpNotificationEvent;
import rw.ac.auca.wedding.Wedding;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RsvpServiceTest {

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private RsvpRepository rsvpRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private WeddingCeremonyRepository ceremonyRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private RsvpService rsvpService;

    private Wedding wedding;
    private Guest guest;
    private Invitation invitation;
    private String token;

    @BeforeEach
    void setUp() {
        token = UUID.randomUUID().toString();

        wedding = Wedding.builder()
                .title("Divine & Jean Ubukwe")
                .build();
        wedding.setId(10L);

        guest = Guest.builder()
                .wedding(wedding)
                .fullName("Aline Keza")
                .plusOneAllowed(1)
                .status(RsvpStatus.PENDING)
                .category(GuestCategory.VIP)
                .build();
        guest.setId(301L);

        invitation = Invitation.builder()
                .wedding(wedding)
                .guest(guest)
                .token(token)
                .personalMessage("We warmly welcome you!")
                .build();
        invitation.setId(401L);
    }

    @Test
    void getPublicInvitationDetails_ExposesOnlySafePublicInformation() {
        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));
        when(ceremonyRepository.findByWeddingId(10L)).thenReturn(List.of());

        PublicInvitationDetailsResponse details = rsvpService.getPublicInvitationDetails(token);

        assertNotNull(details);
        assertEquals(token, details.getToken());
        assertEquals("Divine & Jean Ubukwe", details.getWeddingTitle());
        assertEquals("Aline Keza", details.getGuestName());
        assertEquals(1, details.getPlusOneAllowed());
        assertEquals(RsvpStatus.PENDING, details.getStatus());
    }

    @Test
    void submitRsvp_ThrowsException_WhenPlusOneCountExceedsLimit() {
        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));

        SubmitRsvpRequest req = SubmitRsvpRequest.builder()
                .status(RsvpStatus.ATTENDING)
                .plusOneNames("Friend 1, Friend 2") // 2 plus-ones when max allowed is 1
                .build();

        assertThrows(IllegalArgumentException.class, () -> rsvpService.submitRsvp(token, req));
    }

    @Test
    void submitRsvp_Success_UpdatesGuestStatusAndPublishesRabbitMQEvent() {
        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));
        when(rsvpRepository.findByInvitationId(401L)).thenReturn(Optional.empty());
        when(rsvpRepository.save(any(Rsvp.class))).thenAnswer(i -> i.getArgument(0));

        SubmitRsvpRequest req = SubmitRsvpRequest.builder()
                .status(RsvpStatus.ATTENDING)
                .plusOneNames("Clarisse Uwamahoro")
                .dietaryPreferences("Vegetarian")
                .build();

        PublicInvitationDetailsResponse resp = rsvpService.submitRsvp(token, req);

        assertNotNull(resp);
        assertEquals(RsvpStatus.ATTENDING, guest.getStatus());
        verify(guestRepository, times(1)).save(guest);
        verify(eventPublisher, times(1)).publishRsvpNotification(any(RsvpNotificationEvent.class));
    }
}
