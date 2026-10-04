package rw.ac.auca.timeline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimelineItemRepository extends JpaRepository<TimelineItem, Long> {

    List<TimelineItem> findByWeddingIdOrderByTargetDateAscTargetTimeAsc(Long weddingId);

    List<TimelineItem> findByWeddingIdAndCeremonyIdOrderByTargetDateAscTargetTimeAsc(Long weddingId, Long ceremonyId);

    Optional<TimelineItem> findByIdAndWeddingId(Long id, Long weddingId);
}
