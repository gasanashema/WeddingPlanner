package rw.ac.auca.nosql.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLogDocument>>> getAuditLogs(
            @RequestParam(required = false) String userEmail,
            Authentication authentication) {
        
        String currentEmail = authentication != null ? authentication.getName() : null;
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<AuditLogDocument> logs;
        if (isAdmin && userEmail != null && !userEmail.isBlank()) {
            logs = auditLogService.getAuditLogsForUser(userEmail);
        } else if (isAdmin) {
            logs = auditLogService.getAllAuditLogs();
        } else if (currentEmail != null) {
            logs = auditLogService.getAuditLogsForUser(currentEmail);
        } else {
            logs = List.of();
        }

        return ResponseEntity.ok(ApiResponse.success(logs));
    }
}
