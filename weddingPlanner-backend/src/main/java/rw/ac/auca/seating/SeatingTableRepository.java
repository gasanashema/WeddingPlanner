package rw.ac.auca.seating;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatingTableRepository extends JpaRepository<SeatingTable, Long> {

    @EntityGraph(attributePaths = {"guestSeatings", "guestSeatings.guest", "ceremony"})
    List<SeatingTable> findByWeddingId(Long weddingId);

    @EntityGraph(attributePaths = {"guestSeatings", "guestSeatings.guest", "ceremony"})
    List<SeatingTable> findByWeddingIdAndCeremonyId(Long weddingId, Long ceremonyId);

    @EntityGraph(attributePaths = {"guestSeatings", "guestSeatings.guest", "ceremony"})
    Optional<SeatingTable> findByIdAndWeddingId(Long id, Long weddingId);
}

