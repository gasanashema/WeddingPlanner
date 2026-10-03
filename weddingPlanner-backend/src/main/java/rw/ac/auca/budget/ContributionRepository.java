package rw.ac.auca.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    List<Contribution> findByWeddingId(Long weddingId);

    List<Contribution> findByWeddingIdAndStatus(Long weddingId, ContributionStatus status);

    Optional<Contribution> findByMomoTransactionId(String momoTransactionId);
}
