package rw.ac.auca.nosql.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDocument {

    @Id
    private String id;
    private String userEmail;
    private String action;
    private String resource;
    private String ipAddress;
    private String status;
    private Map<String, Object> details;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
