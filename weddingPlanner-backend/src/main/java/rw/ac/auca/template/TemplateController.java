package rw.ac.auca.template;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.common.ApiResponse;
import rw.ac.auca.homeprep.dto.HomePrepResponse;
import rw.ac.auca.template.dto.ApplyTemplateRequest;
import rw.ac.auca.template.dto.TemplateResponse;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TemplateResponse>>> getAllTemplates() {
        List<TemplateResponse> templates = templateService.getAllActiveTemplates();
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TemplateResponse>> getTemplateById(@PathVariable Long id) {
        TemplateResponse template = templateService.getTemplateById(id);
        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<ApiResponse<List<HomePrepResponse>>> applyTemplate(
            @PathVariable Long id,
            @RequestBody(required = false) ApplyTemplateRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        List<HomePrepResponse> createdItems = templateService.applyTemplateToWedding(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Template applied successfully", createdItems));
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
