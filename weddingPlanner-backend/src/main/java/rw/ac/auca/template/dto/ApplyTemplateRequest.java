package rw.ac.auca.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.wedding.WeddingSide;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyTemplateRequest {

    private Long weddingId;
    private WeddingSide side;
}
