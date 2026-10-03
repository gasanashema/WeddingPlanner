package rw.ac.auca.wedding;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.dto.CreateWeddingRequest;
import rw.ac.auca.wedding.dto.JoinWeddingRequest;
import rw.ac.auca.wedding.dto.UpdateWeddingRequest;
import rw.ac.auca.wedding.dto.WeddingResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weddings")
@RequiredArgsConstructor
public class WeddingController {

    private final WeddingService weddingService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<WeddingResponse>> createWedding(
            @RequestBody CreateWeddingRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        WeddingResponse response = weddingService.createWedding(request, currentUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Wedding created successfully", response));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<WeddingResponse>> joinWedding(
            @Valid @RequestBody JoinWeddingRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication required to join a wedding"));
        }
        try {
            WeddingResponse response = weddingService.joinWedding(request, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Successfully joined wedding", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<WeddingResponse>> getCurrentUserWedding(Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication required"));
        }
        WeddingResponse response = weddingService.getCurrentUserWedding(currentUser);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("No active wedding found for current user"));
        }
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WeddingResponse>>> getAllWeddings() {
        List<WeddingResponse> response = weddingService.getAllWeddings();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WeddingResponse>> getWeddingById(@PathVariable Long id) {
        WeddingResponse response = weddingService.getWeddingById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WeddingResponse>> updateWedding(
            @PathVariable Long id,
            @RequestBody UpdateWeddingRequest request) {
        WeddingResponse response = weddingService.updateWedding(id, request);
        return ResponseEntity.ok(ApiResponse.success("Wedding updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWedding(@PathVariable Long id) {
        weddingService.deleteWedding(id);
        return ResponseEntity.ok(ApiResponse.success("Wedding deleted successfully", null));
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
