package rw.ac.auca.seating;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.guest.Guest;
import rw.ac.auca.guest.GuestCategory;
import rw.ac.auca.guest.GuestRepository;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.seating.dto.AssignGuestSeatingRequest;
import rw.ac.auca.seating.dto.CreateSeatingTableRequest;
import rw.ac.auca.seating.dto.GuestSeatingResponse;
import rw.ac.auca.seating.dto.SeatingOverviewResponse;
import rw.ac.auca.seating.dto.SeatingTableResponse;
import rw.ac.auca.seating.dto.UpdateSeatingTableRequest;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatingServiceTest {

    @Mock
    private SeatingTableRepository seatingTableRepository;

    @Mock
    private GuestSeatingRepository guestSeatingRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private WeddingCeremonyRepository weddingCeremonyRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @InjectMocks
    private SeatingService seatingService;

    private User currentUser;
    private Wedding wedding;
    private WeddingMember member;
    private SeatingTable table;
    private Guest guestBride;
    private Guest guestGroom;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .email("test@example.com")
                .role(Role.ROLE_BRIDE)
                .build();
        currentUser.setId(1L);

        wedding = Wedding.builder()
                .title("Jane & John Wedding")
                .build();
        wedding.setId(10L);

        member = WeddingMember.builder()
                .wedding(wedding)
                .user(currentUser)
                .side(WeddingSide.BRIDE_SIDE)
                .build();
        member.setId(100L);

        table = SeatingTable.builder()
                .wedding(wedding)
                .tableName("VIP Table 1")
                .capacity(8)
                .build();
        table.setId(50L);

        guestBride = Guest.builder()
                .wedding(wedding)
                .fullName("Alice Smith")
                .side(WeddingSide.BRIDE_SIDE)
                .category(GuestCategory.FAMILY)
                .status(RsvpStatus.CONFIRMED)
                .plusOneAllowed(1)
                .build();
        guestBride.setId(200L);

        guestGroom = Guest.builder()
                .wedding(wedding)
                .fullName("Bob Jones")
                .side(WeddingSide.GROOM_SIDE)
                .category(GuestCategory.FRIEND)
                .status(RsvpStatus.CONFIRMED)
                .plusOneAllowed(0)
                .build();
        guestGroom.setId(201L);
    }

    @Test
    void createSeatingTable_Success() {
        CreateSeatingTableRequest req = CreateSeatingTableRequest.builder()
                .weddingId(10L)
                .tableName("Family Table")
                .capacity(10)
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(seatingTableRepository.save(any(SeatingTable.class))).thenAnswer(i -> {
            SeatingTable t = i.getArgument(0);
            t.setId(51L);
            return t;
        });

        SeatingTableResponse response = seatingService.createSeatingTable(req, currentUser);

        assertNotNull(response);
        assertEquals(51L, response.getId());
        assertEquals("Family Table", response.getTableName());
        assertEquals(10, response.getCapacity());
        assertFalse(response.isOverflowing());
    }

    @Test
    void updateSeatingTable_Success() {
        UpdateSeatingTableRequest req = UpdateSeatingTableRequest.builder()
                .tableName("VIP Table Updated")
                .capacity(12)
                .build();

        when(seatingTableRepository.findById(50L)).thenReturn(Optional.of(table));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(seatingTableRepository.save(any(SeatingTable.class))).thenReturn(table);
        when(guestSeatingRepository.findByTableId(50L)).thenReturn(List.of());

        SeatingTableResponse response = seatingService.updateSeatingTable(50L, req, currentUser);

        assertNotNull(response);
        assertEquals("VIP Table Updated", response.getTableName());
        assertEquals(12, response.getCapacity());
    }

    @Test
    void assignGuestToTable_Success() {
        AssignGuestSeatingRequest req = AssignGuestSeatingRequest.builder()
                .guestId(200L)
                .build();

        when(seatingTableRepository.findById(50L)).thenReturn(Optional.of(table));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(guestRepository.findByIdAndDeletedAtIsNull(200L)).thenReturn(Optional.of(guestBride));
        when(guestSeatingRepository.existsByGuestId(200L)).thenReturn(false);
        when(guestSeatingRepository.save(any(GuestSeating.class))).thenAnswer(i -> {
            GuestSeating gs = i.getArgument(0);
            gs.setId(300L);
            gs.setAssignedAt(Instant.now());
            return gs;
        });

        GuestSeatingResponse response = seatingService.assignGuestToTable(50L, req, currentUser);

        assertNotNull(response);
        assertEquals(300L, response.getSeatingId());
        assertEquals(200L, response.getGuestId());
        assertEquals("Alice Smith", response.getGuestName());
        assertEquals(2, response.getTotalSeatsOccupied()); // 1 guest + 1 plusOne
    }

    @Test
    void assignGuestToTable_AlreadyAssigned_ThrowsException() {
        AssignGuestSeatingRequest req = AssignGuestSeatingRequest.builder()
                .guestId(200L)
                .build();

        when(seatingTableRepository.findById(50L)).thenReturn(Optional.of(table));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(guestRepository.findByIdAndDeletedAtIsNull(200L)).thenReturn(Optional.of(guestBride));
        when(guestSeatingRepository.existsByGuestId(200L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                seatingService.assignGuestToTable(50L, req, currentUser)
        );
        assertTrue(ex.getMessage().contains("already assigned"));
    }

    @Test
    void assignGuestToTable_OppositeSide_ThrowsException() {
        AssignGuestSeatingRequest req = AssignGuestSeatingRequest.builder()
                .guestId(201L)
                .build();

        when(seatingTableRepository.findById(50L)).thenReturn(Optional.of(table));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member)); // BRIDE_SIDE member
        when(guestRepository.findByIdAndDeletedAtIsNull(201L)).thenReturn(Optional.of(guestGroom)); // GROOM_SIDE guest

        assertThrows(AccessDeniedException.class, () ->
                seatingService.assignGuestToTable(50L, req, currentUser)
        );
    }

    @Test
    void getSeatingOverview_WithOverflowConflict() {
        table.setCapacity(2); // Small capacity

        GuestSeating seating = GuestSeating.builder()
                .id(300L)
                .table(table)
                .guest(guestBride) // guestBride has plusOneAllowed=1 (occupies 2 seats)
                .assignedAt(Instant.now())
                .build();

        Guest guestBride2 = Guest.builder()
                .wedding(wedding)
                .fullName("Carol White")
                .side(WeddingSide.BRIDE_SIDE)
                .category(GuestCategory.FRIEND)
                .status(RsvpStatus.CONFIRMED)
                .plusOneAllowed(1) // occupies 2 seats
                .build();
        guestBride2.setId(202L);

        GuestSeating seating2 = GuestSeating.builder()
                .id(301L)
                .table(table)
                .guest(guestBride2)
                .assignedAt(Instant.now())
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(seatingTableRepository.findByWeddingId(10L)).thenReturn(List.of(table));
        when(guestRepository.findByWeddingIdAndSideInAndDeletedAtIsNull(eq(10L), anyList()))
                .thenReturn(List.of(guestBride, guestBride2));
        when(guestSeatingRepository.findByGuestIdIn(anyList())).thenReturn(List.of(seating, seating2));

        SeatingOverviewResponse overview = seatingService.getSeatingOverview(10L, currentUser);

        assertNotNull(overview);
        assertEquals(1, overview.getTotalTables());
        assertEquals(2, overview.getTotalCapacity());
        assertEquals(4, overview.getTotalOccupiedSeats()); // 2 + 2 = 4
        assertEquals(2, overview.getTotalSeatedGuestsCount());
        assertEquals(0, overview.getTotalUnassignedGuestsCount());
        assertEquals(1, overview.getOverflowConflictsCount());
        assertTrue(overview.getTables().get(0).isOverflowing());
    }

    @Test
    void unassignGuestFromTable_Success() {
        when(seatingTableRepository.findById(50L)).thenReturn(Optional.of(table));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(guestRepository.findByIdAndDeletedAtIsNull(200L)).thenReturn(Optional.of(guestBride));

        seatingService.unassignGuestFromTable(50L, 200L, currentUser);

        verify(guestSeatingRepository, times(1)).deleteByGuestId(200L);
    }
}
