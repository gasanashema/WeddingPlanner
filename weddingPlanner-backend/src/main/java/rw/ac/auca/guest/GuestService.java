package rw.ac.auca.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.guest.dto.CreateGuestRequest;
import rw.ac.auca.guest.dto.GuestResponse;
import rw.ac.auca.guest.dto.UpdateGuestRequest;
import rw.ac.auca.invitation.Invitation;
import rw.ac.auca.invitation.InvitationRepository;
import rw.ac.auca.messaging.EventPublisher;
import rw.ac.auca.messaging.dto.EmailInvitationEvent;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestService {

    private final GuestRepository guestRepository;
    private final InvitationRepository invitationRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;
    private final EventPublisher eventPublisher;

    public GuestResponse createGuest(CreateGuestRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        WeddingSide guestSide = request.getSide() != null ? request.getSide() : userSide;
        if (!canUserAccessSide(userSide, guestSide)) {
            throw new AccessDeniedException("Forbidden: You cannot create a guest for the opposite side.");
        }

        Guest guest = Guest.builder()
                .wedding(wedding)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .side(guestSide)
                .category(request.getCategory() != null ? request.getCategory() : GuestCategory.FRIEND)
                .plusOneAllowed(request.getPlusOneAllowed() != null ? request.getPlusOneAllowed() : 0)
                .status(request.getStatus() != null ? request.getStatus() : RsvpStatus.PENDING)
                .build();

        Guest savedGuest = guestRepository.save(guest);

        // Generate invitation record with 36-character UUID token
        String token = UUID.randomUUID().toString();
        String shareableUrl = "/rsvp/" + token;

        Invitation invitation = Invitation.builder()
                .wedding(wedding)
                .guest(savedGuest)
                .token(token)
                .title("Wedding Invitation for " + savedGuest.getFullName())
                .personalMessage("You are cordially invited to celebrate our wedding!")
                .shareableUrl(shareableUrl)
                .build();

        invitationRepository.save(invitation);

        // Publish RabbitMQ email event if guest email is present
        if (savedGuest.getEmail() != null && !savedGuest.getEmail().isBlank()) {
            eventPublisher.publishEmailInvitation(EmailInvitationEvent.builder()
                    .invitationToken(token)
                    .guestEmail(savedGuest.getEmail())
                    .guestName(savedGuest.getFullName())
                    .weddingTitle(wedding.getTitle())
                    .shareableUrl(shareableUrl)
                    .build());
        }

        return GuestResponse.fromEntity(savedGuest, token, shareableUrl);
    }

    @Transactional(readOnly = true)
    public List<GuestResponse> getGuestsForUser(User currentUser) {
        Wedding wedding = getUserActiveWedding(currentUser);
        if (wedding == null) {
            return List.of();
        }

        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);
        List<WeddingSide> allowedSides = getAllowedSides(userSide);

        List<Guest> guests = guestRepository.findByWeddingIdAndSideInAndDeletedAtIsNull(wedding.getId(), allowedSides);

        return guests.stream().map(g -> {
            Optional<Invitation> inv = invitationRepository.findByGuestId(g.getId());
            String token = inv.map(Invitation::getToken).orElse(null);
            String url = inv.map(Invitation::getShareableUrl).orElse(null);
            return GuestResponse.fromEntity(g, token, url);
        }).toList();
    }

    @Transactional(readOnly = true)
    public GuestResponse getGuestById(Long id, User currentUser) {
        Guest guest = guestRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Guest not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(guest.getWedding().getId(), currentUser);
        if (!canUserAccessSide(userSide, guest.getSide())) {
            throw new AccessDeniedException("Forbidden: Access denied to guest of the opposite side.");
        }

        Optional<Invitation> inv = invitationRepository.findByGuestId(guest.getId());
        String token = inv.map(Invitation::getToken).orElse(null);
        String url = inv.map(Invitation::getShareableUrl).orElse(null);

        return GuestResponse.fromEntity(guest, token, url);
    }

    public GuestResponse updateGuest(Long id, UpdateGuestRequest request, User currentUser) {
        Guest guest = guestRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Guest not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(guest.getWedding().getId(), currentUser);
        if (!canUserAccessSide(userSide, guest.getSide())) {
            throw new AccessDeniedException("Forbidden: Access denied to guest of the opposite side.");
        }

        if (request.getFullName() != null) {
            guest.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            guest.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            guest.setEmail(request.getEmail());
        }
        if (request.getSide() != null) {
            if (!canUserAccessSide(userSide, request.getSide())) {
                throw new AccessDeniedException("Forbidden: Cannot change guest side to opposite side.");
            }
            guest.setSide(request.getSide());
        }
        if (request.getCategory() != null) {
            guest.setCategory(request.getCategory());
        }
        if (request.getPlusOneAllowed() != null) {
            guest.setPlusOneAllowed(request.getPlusOneAllowed());
        }
        if (request.getStatus() != null) {
            guest.setStatus(request.getStatus());
        }

        Guest updated = guestRepository.save(guest);

        Optional<Invitation> inv = invitationRepository.findByGuestId(updated.getId());
        String token = inv.map(Invitation::getToken).orElse(null);
        String url = inv.map(Invitation::getShareableUrl).orElse(null);

        return GuestResponse.fromEntity(updated, token, url);
    }

    public void deleteGuest(Long id, User currentUser) {
        Guest guest = guestRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Guest not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(guest.getWedding().getId(), currentUser);
        if (!canUserAccessSide(userSide, guest.getSide())) {
            throw new AccessDeniedException("Forbidden: Access denied to guest of the opposite side.");
        }

        guest.setDeletedAt(LocalDateTime.now());
        guestRepository.save(guest);
    }

    private Wedding resolveWeddingForUser(Long requestedWeddingId, User currentUser) {
        if (requestedWeddingId != null) {
            return weddingRepository.findById(requestedWeddingId)
                    .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + requestedWeddingId));
        }
        Wedding active = getUserActiveWedding(currentUser);
        if (active == null) {
            throw new IllegalArgumentException("User has no active wedding workspace.");
        }
        return active;
    }

    private Wedding getUserActiveWedding(User currentUser) {
        if (currentUser == null) return null;
        List<WeddingMember> memberships = weddingMemberRepository.findByUserId(currentUser.getId());
        return memberships.isEmpty() ? null : memberships.get(0).getWedding();
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
        return List.of(WeddingSide.SHARED);
    }

    private boolean canUserAccessSide(WeddingSide userSide, WeddingSide guestSide) {
        if (guestSide == WeddingSide.SHARED) return true;
        if (userSide == WeddingSide.BRIDE_SIDE && guestSide == WeddingSide.BRIDE_SIDE) return true;
        if (userSide == WeddingSide.GROOM_SIDE && guestSide == WeddingSide.GROOM_SIDE) return true;
        return false;
    }
}
