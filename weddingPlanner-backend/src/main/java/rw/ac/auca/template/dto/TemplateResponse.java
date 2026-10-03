package rw.ac.auca.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.template.PlanningTemplate;
import rw.ac.auca.template.TemplateCategory;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse {

    private Long id;
    private String name;
    private TemplateCategory category;
    private String description;
    private boolean isActive;
    private List<TemplateItemResponse> items;

    public static TemplateResponse fromEntity(PlanningTemplate entity) {
        if (entity == null) return null;
        List<TemplateItemResponse> itemResponses = entity.getItems() != null ?
                entity.getItems().stream().map(TemplateItemResponse::fromEntity).toList() : List.of();

        return TemplateResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .isActive(entity.isActive())
                .items(itemResponses)
                .build();
    }
}
