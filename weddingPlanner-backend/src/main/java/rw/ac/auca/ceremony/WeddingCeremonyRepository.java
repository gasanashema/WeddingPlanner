package rw.ac.auca.ceremony;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeddingCeremonyRepository extends JpaRepository<WeddingCeremony, Long> {
    List<WeddingCeremony> findByWeddingId(Long weddingId);
}
