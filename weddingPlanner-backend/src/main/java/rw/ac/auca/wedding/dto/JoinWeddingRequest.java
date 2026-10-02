package rw.ac.auca.wedding.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinWeddingRequest {

    @NotBlank(message = "Join code (partner code or family code) is required")
    private String joinCode;

    private String role; // Optional requested role: BRIDE_SUPPORT, GROOM_SUPPORT, BRIDE, GROOM
}
