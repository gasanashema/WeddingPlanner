package rw.ac.auca.timeline;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.common.ApiResponse;
import rw.ac.auca.timeline.dto.CreateTimelineItemRequest;
import rw.ac.auca.timeline.dto.TimelineItemResponse;
import rw.ac.auca.timeline.dto.UpdateTimelineItemRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timelines")
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineService timelineService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TimelineItemResponse>>> getTimelineItems(
            @RequestParam(required = false) Long weddingId,
            @RequestParam(required = false) Long ceremonyId,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            List<TimelineItemResponse> items = timelineService.getTimelineItems(weddingId, ceremonyId, currentUser);
            return ResponseEntity.ok(ApiResponse.success(items));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TimelineItemResponse>> createTimelineItem(
            @Valid @RequestBody CreateTimelineItemRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            TimelineItemResponse response = timelineService.createTimelineItem(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Timeline item created successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TimelineItemResponse>> updateTimelineItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTimelineItemRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            TimelineItemResponse response = timelineService.updateTimelineItem(id, request, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Timeline item updated successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<TimelineItemResponse>> toggleTimelineItemCompletion(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            TimelineItemResponse response = timelineService.toggleTimelineItemCompletion(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Timeline item status toggled successfully", response));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTimelineItem(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            timelineService.deleteTimelineItem(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Timeline item deleted successfully", null));
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
