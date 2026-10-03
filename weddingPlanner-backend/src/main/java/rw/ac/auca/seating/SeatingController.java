package rw.ac.auca.seating;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.common.ApiResponse;
import rw.ac.auca.seating.dto.AssignGuestSeatingRequest;
import rw.ac.auca.seating.dto.CreateSeatingTableRequest;
import rw.ac.auca.seating.dto.GuestSeatingResponse;
import rw.ac.auca.seating.dto.SeatingOverviewResponse;
import rw.ac.auca.seating.dto.SeatingTableResponse;
import rw.ac.auca.seating.dto.UpdateSeatingTableRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class SeatingController {

    private final SeatingService seatingService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<SeatingOverviewResponse>> getSeatingOverview(
            @RequestParam(required = false) Long weddingId,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            SeatingOverviewResponse overview = seatingService.getSeatingOverview(weddingId, currentUser);
            return ResponseEntity.ok(ApiResponse.success(overview));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SeatingTableResponse>> createSeatingTable(
            @Valid @RequestBody CreateSeatingTableRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            SeatingTableResponse response = seatingService.createSeatingTable(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Seating table created successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SeatingTableResponse>> updateSeatingTable(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatingTableRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            SeatingTableResponse response = seatingService.updateSeatingTable(id, request, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Seating table updated successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSeatingTable(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            seatingService.deleteSeatingTable(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Seating table deleted successfully", null));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<GuestSeatingResponse>> assignGuestToTable(
            @PathVariable Long id,
            @Valid @RequestBody AssignGuestSeatingRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            GuestSeatingResponse response = seatingService.assignGuestToTable(id, request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Guest assigned to table successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/unassign/{guestId}")
    public ResponseEntity<ApiResponse<Void>> unassignGuestFromTable(
            @PathVariable Long id,
            @PathVariable Long guestId,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            seatingService.unassignGuestFromTable(id, guestId, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Guest unassigned from table successfully", null));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
