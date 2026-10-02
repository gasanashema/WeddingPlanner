package rw.ac.auca.wedding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingSide;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeddingMemberResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String role;
    private WeddingSide side;
    private LocalDateTime joinedAt;

    public static WeddingMemberResponse fromEntity(WeddingMember member) {
        if (member == null) return null;
        String name = member.getUser() != null 
                ? member.getUser().getFirstName() + " " + member.getUser().getLastName()
                : null;
        String email = member.getUser() != null ? member.getUser().getEmail() : null;

        return WeddingMemberResponse.builder()
                .id(member.getId())
                .userId(member.getUser() != null ? member.getUser().getId() : null)
                .userName(name)
                .userEmail(email)
                .role(member.getRole())
                .side(member.getSide())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
