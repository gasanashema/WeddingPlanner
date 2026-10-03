package rw.ac.auca.homeprep;

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
import rw.ac.auca.homeprep.dto.CreateHomePrepRequest;
import rw.ac.auca.homeprep.dto.HomePrepResponse;
import rw.ac.auca.homeprep.dto.UpdateHomePrepRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/home-prep")
@RequiredArgsConstructor
public class HomePrepController {

    private final HomePrepService homePrepService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<HomePrepResponse>> createHomePrep(
            @Valid @RequestBody CreateHomePrepRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            HomePrepResponse response = homePrepService.createHomePrep(request, currentUser);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Household preparation item created successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HomePrepResponse>>> getHomePreps(Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        List<HomePrepResponse> response = homePrepService.getHomePrepsForUser(currentUser);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HomePrepResponse>> getHomePrepById(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            HomePrepResponse response = homePrepService.getHomePrepById(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HomePrepResponse>> updateHomePrep(
            @PathVariable Long id,
            @RequestBody UpdateHomePrepRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            HomePrepResponse response = homePrepService.updateHomePrep(id, request, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Household preparation item updated successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHomePrep(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            homePrepService.deleteHomePrep(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Household preparation item deleted successfully", null));
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
