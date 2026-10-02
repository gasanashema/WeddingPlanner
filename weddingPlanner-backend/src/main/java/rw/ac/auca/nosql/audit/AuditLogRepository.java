package rw.ac.auca.nosql.audit;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLogDocument, String> {
    List<AuditLogDocument> findByUserEmailOrderByTimestampDesc(String userEmail);
    List<AuditLogDocument> findByActionOrderByTimestampDesc(String action);
}
