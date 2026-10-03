package rw.ac.auca.guest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.guest.Guest;
import rw.ac.auca.guest.GuestCategory;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.wedding.WeddingSide;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestResponse {

    private Long id;
    private Long weddingId;
    private String fullName;
    private String phone;
    private String email;
    private WeddingSide side;
    private GuestCategory category;
    private int plusOneAllowed;
    private RsvpStatus status;
    private String invitationToken;
    private String shareableUrl;
    private Instant createdAt;
    private Instant updatedAt;

    public static GuestResponse fromEntity(Guest entity, String token, String shareableUrl) {
        if (entity == null) return null;

        return GuestResponse.builder()
                .id(entity.getId())
                .weddingId(entity.getWedding() != null ? entity.getWedding().getId() : null)
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .side(entity.getSide())
                .category(entity.getCategory())
                .plusOneAllowed(entity.getPlusOneAllowed())
                .status(entity.getStatus())
                .invitationToken(token)
                .shareableUrl(shareableUrl)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
