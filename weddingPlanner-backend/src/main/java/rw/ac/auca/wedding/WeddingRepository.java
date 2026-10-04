package rw.ac.auca.wedding;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeddingRepository extends JpaRepository<Wedding, Long> {

    @EntityGraph(attributePaths = {"bride", "groom"})
    Optional<Wedding> findByPartnerCode(String partnerCode);

    @EntityGraph(attributePaths = {"bride", "groom"})
    Optional<Wedding> findByFamilyCode(String familyCode);

    @EntityGraph(attributePaths = {"bride", "groom"})
    Optional<Wedding> findById(Long id);
}

