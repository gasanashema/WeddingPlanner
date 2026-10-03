package rw.ac.auca.guest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.common.ApiResponse;
import rw.ac.auca.guest.dto.CreateGuestRequest;
import rw.ac.auca.guest.dto.GuestResponse;
import rw.ac.auca.guest.dto.UpdateGuestRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<GuestResponse>> createGuest(
            @Valid @RequestBody CreateGuestRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            GuestResponse response = guestService.createGuest(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Guest added successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GuestResponse>>> getGuests(Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        List<GuestResponse> response = guestService.getGuestsForUser(currentUser);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GuestResponse>> getGuestById(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            GuestResponse response = guestService.getGuestById(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GuestResponse>> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGuestRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            GuestResponse response = guestService.updateGuest(id, request, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Guest updated successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGuest(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            guestService.deleteGuest(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Guest soft-deleted successfully", null));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
