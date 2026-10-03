package rw.ac.auca.homeprep.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.homeprep.HomePrepCategory;
import rw.ac.auca.homeprep.HomePreparation;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePrepResponse {

    private Long id;
    private Long weddingId;
    private HomePrepCategory category;
    private String itemName;
    private WeddingSide side;
    private Long assignedUserId;
    private String assignedUserName;
    private BigDecimal budgetRwf;
    private LocalDate dueDate;
    private boolean isCompleted;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;

    public static HomePrepResponse fromEntity(HomePreparation entity) {
        if (entity == null) return null;

        String assignedUserName = null;
        if (entity.getAssignedUser() != null) {
            assignedUserName = entity.getAssignedUser().getFirstName() + " " + entity.getAssignedUser().getLastName();
        }

        return HomePrepResponse.builder()
                .id(entity.getId())
                .weddingId(entity.getWedding() != null ? entity.getWedding().getId() : null)
                .category(entity.getCategory())
                .itemName(entity.getItemName())
                .side(entity.getSide())
                .assignedUserId(entity.getAssignedUser() != null ? entity.getAssignedUser().getId() : null)
                .assignedUserName(assignedUserName)
                .budgetRwf(entity.getBudgetRwf())
                .dueDate(entity.getDueDate())
                .isCompleted(entity.isCompleted())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
