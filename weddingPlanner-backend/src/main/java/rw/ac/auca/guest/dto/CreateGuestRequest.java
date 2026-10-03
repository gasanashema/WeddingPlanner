package rw.ac.auca.guest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.guest.GuestCategory;
import rw.ac.auca.guest.RsvpStatus;
import rw.ac.auca.wedding.WeddingSide;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGuestRequest {

    private Long weddingId;

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name cannot exceed 120 characters")
    private String fullName;

    private String phone;
    private String email;
    private WeddingSide side;
    private GuestCategory category;

    @Min(value = 0, message = "Plus one allowed cannot be negative")
    private Integer plusOneAllowed;

    private RsvpStatus status;
}
