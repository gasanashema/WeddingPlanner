package rw.ac.auca.nosql.activity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends MongoRepository<ActivityLogDocument, String> {
    List<ActivityLogDocument> findByWeddingIdOrderByTimestampDesc(Long weddingId);
}
