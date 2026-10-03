package rw.ac.auca.seating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatingTableRepository extends JpaRepository<SeatingTable, Long> {

    List<SeatingTable> findByWeddingId(Long weddingId);

    List<SeatingTable> findByWeddingIdAndCeremonyId(Long weddingId, Long ceremonyId);

    Optional<SeatingTable> findByIdAndWeddingId(Long id, Long weddingId);
}
