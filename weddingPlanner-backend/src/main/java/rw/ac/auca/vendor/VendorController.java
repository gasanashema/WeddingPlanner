package rw.ac.auca.vendor;

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
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.vendor.dto.CreateVendorRequest;
import rw.ac.auca.vendor.dto.UpdateVendorRequest;
import rw.ac.auca.vendor.dto.VendorResponse;
import rw.ac.auca.vendor.dto.VendorSummaryResponse;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<VendorSummaryResponse>> getVendors(
            @RequestParam(required = false) Long weddingId,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            VendorSummaryResponse summary = vendorService.getVendorsByWedding(weddingId, currentUser);
            return ResponseEntity.ok(ApiResponse.success(summary));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VendorResponse>> createVendor(
            @Valid @RequestBody CreateVendorRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            VendorResponse response = vendorService.createVendor(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Vendor created successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVendorRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            VendorResponse response = vendorService.updateVendor(id, request, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Vendor updated successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVendor(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            vendorService.deleteVendor(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Vendor deleted successfully", null));
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
