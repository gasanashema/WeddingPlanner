package rw.ac.auca.wedding;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.common.ApiResponse;
import rw.ac.auca.wedding.dto.CreateWeddingRequest;
import rw.ac.auca.wedding.dto.UpdateWeddingRequest;
import rw.ac.auca.wedding.dto.WeddingResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weddings")
@RequiredArgsConstructor
public class WeddingController {

    private final WeddingService weddingService;

    @PostMapping
    public ResponseEntity<ApiResponse<WeddingResponse>> createWedding(@RequestBody CreateWeddingRequest request) {
        WeddingResponse response = weddingService.createWedding(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Wedding created successfully", response));
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
}
