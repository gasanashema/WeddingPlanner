package rw.ac.auca.guest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.guest.dto.CreateGuestRequest;
import rw.ac.auca.guest.dto.GuestResponse;
import rw.ac.auca.invitation.Invitation;
import rw.ac.auca.invitation.InvitationRepository;
import rw.ac.auca.messaging.EventPublisher;
import rw.ac.auca.messaging.dto.EmailInvitationEvent;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private GuestService guestService;

    private User brideUser;
    private User groomUser;
    private Wedding wedding;
    private Guest brideGuest;
    private Guest groomGuest;

    @BeforeEach
    void setUp() {
        brideUser = User.builder()
                .firstName("Divine")
                .lastName("Mutesi")
                .email("divine@wedding.rw")
                .role(Role.ROLE_BRIDE)
                .build();
        brideUser.setId(1L);

        groomUser = User.builder()
                .firstName("Jean")
                .lastName("Mugisha")
                .email("jean@wedding.rw")
                .role(Role.ROLE_GROOM)
                .build();
        groomUser.setId(2L);

        wedding = Wedding.builder()
                .title("Divine & Jean Ubukwe")
                .bride(brideUser)
                .groom(groomUser)
                .build();
        wedding.setId(10L);

        brideGuest = Guest.builder()
                .wedding(wedding)
                .fullName("Aline Keza")
                .email("aline@guest.rw")
                .side(WeddingSide.BRIDE_SIDE)
                .category(GuestCategory.VIP)
                .plusOneAllowed(2)
                .status(RsvpStatus.PENDING)
                .build();
        brideGuest.setId(301L);

        groomGuest = Guest.builder()
                .wedding(wedding)
                .fullName("Eric Manzi")
                .email("eric@guest.rw")
                .side(WeddingSide.GROOM_SIDE)
                .category(GuestCategory.FRIEND)
                .plusOneAllowed(1)
                .status(RsvpStatus.PENDING)
                .build();
        groomGuest.setId(302L);
    }

    @Test
    void createGuest_Generates36CharUuidToken_AndPublishesRabbitMQEvent() {
        CreateGuestRequest req = CreateGuestRequest.builder()
                .weddingId(10L)
                .fullName("Aline Keza")
                .email("aline@guest.rw")
                .side(WeddingSide.BRIDE_SIDE)
                .category(GuestCategory.VIP)
                .plusOneAllowed(2)
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(guestRepository.save(any(Guest.class))).thenAnswer(i -> {
            Guest g = i.getArgument(0);
            g.setId(301L);
            return g;
        });

        GuestResponse resp = guestService.createGuest(req, brideUser);

        assertNotNull(resp);
        assertEquals("Aline Keza", resp.getFullName());
        assertNotNull(resp.getInvitationToken());
        assertEquals(36, resp.getInvitationToken().length());
        verify(invitationRepository, times(1)).save(any(Invitation.class));
        verify(eventPublisher, times(1)).publishEmailInvitation(any(EmailInvitationEvent.class));
    }

    @Test
    void brideUser_Gets_Only_BrideSide_And_SharedGuests() {
        when(weddingMemberRepository.findByUserId(1L)).thenReturn(List.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(guestRepository.findByWeddingIdAndSideInAndDeletedAtIsNull(eq(10L), anyList()))
                .thenReturn(List.of(brideGuest));

        List<GuestResponse> guests = guestService.getGuestsForUser(brideUser);

        assertEquals(1, guests.size());
        assertEquals(WeddingSide.BRIDE_SIDE, guests.get(0).getSide());
    }

    @Test
    void groomUser_DeniedAccessTo_BridePrivateGuest() {
        when(guestRepository.findByIdAndDeletedAtIsNull(301L)).thenReturn(Optional.of(brideGuest));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 2L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(groomUser).side(WeddingSide.GROOM_SIDE).build()
        ));

        assertThrows(AccessDeniedException.class, () -> guestService.getGuestById(301L, groomUser));
    }
}
