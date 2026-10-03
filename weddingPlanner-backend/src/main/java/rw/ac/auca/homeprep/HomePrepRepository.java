package rw.ac.auca.homeprep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.wedding.WeddingSide;

import java.util.List;

@Repository
public interface HomePrepRepository extends JpaRepository<HomePreparation, Long> {

    List<HomePreparation> findByWeddingId(Long weddingId);

    List<HomePreparation> findByWeddingIdAndSideIn(Long weddingId, List<WeddingSide> sides);
}
