package rw.ac.auca.ceremony;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.ceremony.dto.CeremonyResponse;
import rw.ac.auca.ceremony.dto.CreateCeremonyRequest;
import rw.ac.auca.ceremony.dto.UpdateCeremonyRequest;
import rw.ac.auca.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ceremonies")
@RequiredArgsConstructor
public class WeddingCeremonyController {

    private final WeddingCeremonyService ceremonyService;

    @PostMapping
    public ResponseEntity<ApiResponse<CeremonyResponse>> createCeremony(@RequestBody CreateCeremonyRequest request) {
        CeremonyResponse response = ceremonyService.createCeremony(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ceremony created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CeremonyResponse>>> getAllCeremonies(
            @RequestParam(required = false) Long weddingId) {
        List<CeremonyResponse> response = ceremonyService.getAllCeremonies(weddingId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CeremonyResponse>> getCeremonyById(@PathVariable Long id) {
        CeremonyResponse response = ceremonyService.getCeremonyById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CeremonyResponse>> updateCeremony(
            @PathVariable Long id,
            @RequestBody UpdateCeremonyRequest request) {
        CeremonyResponse response = ceremonyService.updateCeremony(id, request);
        return ResponseEntity.ok(ApiResponse.success("Ceremony updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCeremony(@PathVariable Long id) {
        ceremonyService.deleteCeremony(id);
        return ResponseEntity.ok(ApiResponse.success("Ceremony deleted successfully", null));
    }
}
