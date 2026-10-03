package rw.ac.auca.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.template.TemplateItem;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateItemResponse {

    private Long id;
    private String title;
    private String itemType;
    private WeddingSide defaultSide;
    private BigDecimal estimatedBudget;

    public static TemplateItemResponse fromEntity(TemplateItem entity) {
        if (entity == null) return null;
        return TemplateItemResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .itemType(entity.getItemType())
                .defaultSide(entity.getDefaultSide())
                .estimatedBudget(entity.getEstimatedBudget())
                .build();
    }
}
