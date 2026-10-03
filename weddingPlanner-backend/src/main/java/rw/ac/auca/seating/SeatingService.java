package rw.ac.auca.seating;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.guest.Guest;
import rw.ac.auca.guest.GuestRepository;
import rw.ac.auca.seating.dto.AssignGuestSeatingRequest;
import rw.ac.auca.seating.dto.CreateSeatingTableRequest;
import rw.ac.auca.seating.dto.GuestSeatingResponse;
import rw.ac.auca.seating.dto.SeatingOverviewResponse;
import rw.ac.auca.seating.dto.SeatingTableResponse;
import rw.ac.auca.seating.dto.UnassignedGuestResponse;
import rw.ac.auca.seating.dto.UpdateSeatingTableRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatingService {

    private final SeatingTableRepository seatingTableRepository;
    private final GuestSeatingRepository guestSeatingRepository;
    private final GuestRepository guestRepository;
    private final WeddingCeremonyRepository weddingCeremonyRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;

    public SeatingTableResponse createSeatingTable(CreateSeatingTableRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        validateUserMembership(wedding.getId(), currentUser);

        WeddingCeremony ceremony = null;
        if (request.getCeremonyId() != null) {
            ceremony = weddingCeremonyRepository.findByIdAndWeddingId(request.getCeremonyId(), wedding.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ceremony not found with id: " + request.getCeremonyId()));
        }

        SeatingTable table = SeatingTable.builder()
                .wedding(wedding)
                .ceremony(ceremony)
                .tableName(request.getTableName())
                .capacity(request.getCapacity() > 0 ? request.getCapacity() : 8)
                .build();

        SeatingTable saved = seatingTableRepository.save(table);
        return mapToSeatingTableResponse(saved, new ArrayList<>());
    }

    public SeatingTableResponse updateSeatingTable(Long tableId, UpdateSeatingTableRequest request, User currentUser) {
        SeatingTable table = seatingTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Seating table not found with id: " + tableId));

        validateUserMembership(table.getWedding().getId(), currentUser);

        WeddingCeremony ceremony = null;
        if (request.getCeremonyId() != null) {
            ceremony = weddingCeremonyRepository.findByIdAndWeddingId(request.getCeremonyId(), table.getWedding().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ceremony not found with id: " + request.getCeremonyId()));
        }

        table.setTableName(request.getTableName());
        table.setCapacity(request.getCapacity() > 0 ? request.getCapacity() : 8);
        table.setCeremony(ceremony);

        SeatingTable updated = seatingTableRepository.save(table);
        List<GuestSeating> seatings = guestSeatingRepository.findByTableId(updated.getId());
        return mapToSeatingTableResponse(updated, seatings);
    }

    public void deleteSeatingTable(Long tableId, User currentUser) {
        SeatingTable table = seatingTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Seating table not found with id: " + tableId));

        validateUserMembership(table.getWedding().getId(), currentUser);
        guestSeatingRepository.deleteByTableId(tableId);
        seatingTableRepository.delete(table);
    }

    public GuestSeatingResponse assignGuestToTable(Long tableId, AssignGuestSeatingRequest request, User currentUser) {
        SeatingTable table = seatingTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Seating table not found with id: " + tableId));

        validateUserMembership(table.getWedding().getId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(table.getWedding().getId(), currentUser);

        Guest guest = guestRepository.findByIdAndDeletedAtIsNull(request.getGuestId())
                .orElseThrow(() -> new IllegalArgumentException("Guest not found with id: " + request.getGuestId()));

        if (!guest.getWedding().getId().equals(table.getWedding().getId())) {
            throw new IllegalArgumentException("Guest does not belong to the same wedding.");
        }

        if (!canUserAccessSide(userSide, guest.getSide())) {
            throw new AccessDeniedException("Forbidden: You cannot assign a guest from the opposite side.");
        }

        if (guestSeatingRepository.existsByGuestId(guest.getId())) {
            throw new IllegalArgumentException("Guest is already assigned to a seating table. Unassign first.");
        }

        GuestSeating seating = GuestSeating.builder()
                .table(table)
                .guest(guest)
                .assignedAt(Instant.now())
                .build();

        GuestSeating saved = guestSeatingRepository.save(seating);

        int totalSeatsOccupied = 1 + guest.getPlusOneAllowed();

        return GuestSeatingResponse.builder()
                .seatingId(saved.getId())
                .tableId(table.getId())
                .tableName(table.getTableName())
                .guestId(guest.getId())
                .guestName(guest.getFullName())
                .guestPhone(guest.getPhone())
                .side(guest.getSide())
                .category(guest.getCategory())
                .status(guest.getStatus())
                .plusOneAllowed(guest.getPlusOneAllowed())
                .totalSeatsOccupied(totalSeatsOccupied)
                .assignedAt(saved.getAssignedAt())
                .build();
    }

    public void unassignGuestFromTable(Long tableId, Long guestId, User currentUser) {
        SeatingTable table = seatingTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Seating table not found with id: " + tableId));

        validateUserMembership(table.getWedding().getId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(table.getWedding().getId(), currentUser);

        Guest guest = guestRepository.findByIdAndDeletedAtIsNull(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found with id: " + guestId));

        if (!canUserAccessSide(userSide, guest.getSide())) {
            throw new AccessDeniedException("Forbidden: You cannot modify guest seating for the opposite side.");
        }

        guestSeatingRepository.deleteByGuestId(guestId);
    }

    @Transactional(readOnly = true)
    public SeatingOverviewResponse getSeatingOverview(Long requestedWeddingId, User currentUser) {
        Wedding wedding = resolveWeddingForUser(requestedWeddingId, currentUser);
        validateUserMembership(wedding.getId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        List<SeatingTable> tables = seatingTableRepository.findByWeddingId(wedding.getId());
        List<WeddingSide> allowedSides = getAllowedSides(userSide);
        List<Guest> allowedGuests = guestRepository.findByWeddingIdAndSideInAndDeletedAtIsNull(wedding.getId(), allowedSides);

        Set<Long> allowedGuestIds = allowedGuests.stream().map(Guest::getId).collect(Collectors.toSet());
        List<GuestSeating> allSeatings = allowedGuestIds.isEmpty() ? new ArrayList<>() : guestSeatingRepository.findByGuestIdIn(new ArrayList<>(allowedGuestIds));

        Map<Long, List<GuestSeating>> seatingsByTableId = allSeatings.stream()
                .collect(Collectors.groupingBy(s -> s.getTable().getId()));

        Set<Long> seatedGuestIds = allSeatings.stream()
                .map(s -> s.getGuest().getId())
                .collect(Collectors.toSet());

        List<SeatingTableResponse> tableResponses = new ArrayList<>();
        int totalCapacity = 0;
        int totalOccupiedSeats = 0;
        int overflowConflictsCount = 0;

        for (SeatingTable table : tables) {
            List<GuestSeating> tableSeatings = seatingsByTableId.getOrDefault(table.getId(), new ArrayList<>());
            SeatingTableResponse response = mapToSeatingTableResponse(table, tableSeatings);
            tableResponses.add(response);

            totalCapacity += table.getCapacity();
            totalOccupiedSeats += response.getOccupiedSeats();
            if (response.isOverflowing()) {
                overflowConflictsCount++;
            }
        }

        List<UnassignedGuestResponse> unassignedGuests = allowedGuests.stream()
                .filter(g -> !seatedGuestIds.contains(g.getId()))
                .map(g -> UnassignedGuestResponse.builder()
                        .guestId(g.getId())
                        .fullName(g.getFullName())
                        .phone(g.getPhone())
                        .email(g.getEmail())
                        .side(g.getSide())
                        .category(g.getCategory())
                        .status(g.getStatus())
                        .plusOneAllowed(g.getPlusOneAllowed())
                        .requiredSeats(1 + g.getPlusOneAllowed())
                        .build())
                .collect(Collectors.toList());

        return SeatingOverviewResponse.builder()
                .totalTables(tables.size())
                .totalCapacity(totalCapacity)
                .totalOccupiedSeats(totalOccupiedSeats)
                .totalSeatedGuestsCount(seatedGuestIds.size())
                .totalUnassignedGuestsCount(unassignedGuests.size())
                .overflowConflictsCount(overflowConflictsCount)
                .tables(tableResponses)
                .unassignedGuests(unassignedGuests)
                .build();
    }

    private SeatingTableResponse mapToSeatingTableResponse(SeatingTable table, List<GuestSeating> seatings) {
        int occupiedSeats = 0;
        List<GuestSeatingResponse> seatedGuestResponses = new ArrayList<>();

        for (GuestSeating seating : seatings) {
            Guest guest = seating.getGuest();
            int seatsForGuest = 1 + guest.getPlusOneAllowed();
            occupiedSeats += seatsForGuest;

            seatedGuestResponses.add(GuestSeatingResponse.builder()
                    .seatingId(seating.getId())
                    .tableId(table.getId())
                    .tableName(table.getTableName())
                    .guestId(guest.getId())
                    .guestName(guest.getFullName())
                    .guestPhone(guest.getPhone())
                    .side(guest.getSide())
                    .category(guest.getCategory())
                    .status(guest.getStatus())
                    .plusOneAllowed(guest.getPlusOneAllowed())
                    .totalSeatsOccupied(seatsForGuest)
                    .assignedAt(seating.getAssignedAt())
                    .build());
        }

        boolean isOverflowing = occupiedSeats > table.getCapacity();

        return SeatingTableResponse.builder()
                .id(table.getId())
                .weddingId(table.getWedding().getId())
                .ceremonyId(table.getCeremony() != null ? table.getCeremony().getId() : null)
                .ceremonyName(table.getCeremony() != null ? table.getCeremony().getName() : null)
                .tableName(table.getTableName())
                .capacity(table.getCapacity())
                .occupiedSeats(occupiedSeats)
                .isOverflowing(isOverflowing)
                .seatedGuests(seatedGuestResponses)
                .createdAt(table.getCreatedAt())
                .updatedAt(table.getUpdatedAt())
                .build();
    }

    private Wedding resolveWeddingForUser(Long requestedWeddingId, User currentUser) {
        if (requestedWeddingId != null) {
            return weddingRepository.findById(requestedWeddingId)
                    .orElseThrow(() -> new IllegalArgumentException("Wedding not found with id: " + requestedWeddingId));
        }
        List<WeddingMember> memberships = weddingMemberRepository.findByUserId(currentUser.getId());
        if (memberships.isEmpty()) {
            throw new IllegalArgumentException("User has no active wedding workspace.");
        }
        return memberships.get(0).getWedding();
    }

    private void validateUserMembership(Long weddingId, User currentUser) {
        if (currentUser == null) return;
        boolean isMember = weddingMemberRepository.findByWeddingIdAndUserId(weddingId, currentUser.getId()).isPresent();
        if (!isMember) {
            throw new AccessDeniedException("Forbidden: You are not a member of this wedding.");
        }
    }

    private WeddingSide getUserSideInWedding(Long weddingId, User currentUser) {
        if (currentUser == null) return WeddingSide.SHARED;
        return weddingMemberRepository.findByWeddingIdAndUserId(weddingId, currentUser.getId())
                .map(WeddingMember::getSide)
                .orElse(WeddingSide.SHARED);
    }

    private List<WeddingSide> getAllowedSides(WeddingSide userSide) {
        if (userSide == WeddingSide.BRIDE_SIDE) {
            return List.of(WeddingSide.BRIDE_SIDE, WeddingSide.SHARED);
        } else if (userSide == WeddingSide.GROOM_SIDE) {
            return List.of(WeddingSide.GROOM_SIDE, WeddingSide.SHARED);
        }
        return List.of(WeddingSide.BRIDE_SIDE, WeddingSide.GROOM_SIDE, WeddingSide.SHARED);
    }

    private boolean canUserAccessSide(WeddingSide userSide, WeddingSide guestSide) {
        if (guestSide == WeddingSide.SHARED) return true;
        if (userSide == WeddingSide.BRIDE_SIDE && guestSide == WeddingSide.BRIDE_SIDE) return true;
        if (userSide == WeddingSide.GROOM_SIDE && guestSide == WeddingSide.GROOM_SIDE) return true;
        return userSide == WeddingSide.SHARED;
    }
}
