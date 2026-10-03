package rw.ac.auca.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.wedding.WeddingSide;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByWeddingIdAndDeletedAtIsNull(Long weddingId);

    List<Guest> findByWeddingIdAndSideInAndDeletedAtIsNull(Long weddingId, List<WeddingSide> sides);

    Optional<Guest> findByIdAndDeletedAtIsNull(Long id);
}
