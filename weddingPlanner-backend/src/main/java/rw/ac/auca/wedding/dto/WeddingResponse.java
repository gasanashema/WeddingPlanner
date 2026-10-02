package rw.ac.auca.wedding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.ceremony.dto.CeremonyResponse;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeddingResponse {
    private Long id;
    private String title;
    private BigDecimal targetBudget;
    private String partnerCode;
    private String familyCode;
    private Long brideId;
    private String brideName;
    private Long groomId;
    private String groomName;
    private List<WeddingMemberResponse> members;
    private List<CeremonyResponse> ceremonies;
    private Instant createdAt;
    private Instant updatedAt;

    public static WeddingResponse fromEntity(Wedding wedding) {
        return fromEntity(wedding, null, null);
    }

    public static WeddingResponse fromEntity(Wedding wedding, List<WeddingMember> members, List<CeremonyResponse> ceremonies) {
        if (wedding == null) return null;

        String brideName = wedding.getBride() != null 
                ? wedding.getBride().getFirstName() + " " + wedding.getBride().getLastName() 
                : null;
        String groomName = wedding.getGroom() != null 
                ? wedding.getGroom().getFirstName() + " " + wedding.getGroom().getLastName() 
                : null;

        List<WeddingMemberResponse> memberResponses = members != null 
                ? members.stream().map(WeddingMemberResponse::fromEntity).toList() 
                : new ArrayList<>();

        return WeddingResponse.builder()
                .id(wedding.getId())
                .title(wedding.getTitle())
                .targetBudget(wedding.getTargetBudget())
                .partnerCode(wedding.getPartnerCode())
                .familyCode(wedding.getFamilyCode())
                .brideId(wedding.getBride() != null ? wedding.getBride().getId() : null)
                .brideName(brideName)
                .groomId(wedding.getGroom() != null ? wedding.getGroom().getId() : null)
                .groomName(groomName)
                .members(memberResponses)
                .ceremonies(ceremonies != null ? ceremonies : new ArrayList<>())
                .createdAt(wedding.getCreatedAt())
                .updatedAt(wedding.getUpdatedAt())
                .build();
    }
}
