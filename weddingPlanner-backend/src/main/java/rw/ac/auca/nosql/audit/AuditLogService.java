package rw.ac.auca.nosql.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(String userEmail, String action, String resource, String ipAddress, String status, Map<String, Object> details) {
        try {
            AuditLogDocument doc = AuditLogDocument.builder()
                    .userEmail(userEmail)
                    .action(action)
                    .resource(resource)
                    .ipAddress(ipAddress)
                    .status(status)
                    .details(details != null ? details : Collections.emptyMap())
                    .build();
            auditLogRepository.save(doc);
        } catch (Exception e) {
            log.warn("Could not save NoSQL audit log to MongoDB: {}", e.getMessage());
        }
    }

    public List<AuditLogDocument> getAuditLogsForUser(String userEmail) {
        try {
            return auditLogRepository.findByUserEmailOrderByTimestampDesc(userEmail);
        } catch (Exception e) {
            log.warn("MongoDB unavailable for audit log fetch: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<AuditLogDocument> getAllAuditLogs() {
        try {
            return auditLogRepository.findAll();
        } catch (Exception e) {
            log.warn("MongoDB unavailable for audit log fetch: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
