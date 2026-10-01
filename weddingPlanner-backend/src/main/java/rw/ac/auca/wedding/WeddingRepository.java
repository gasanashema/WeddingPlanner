package rw.ac.auca.wedding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeddingRepository extends JpaRepository<Wedding, Long> {
    Optional<Wedding> findByPartnerCode(String partnerCode);
    Optional<Wedding> findByFamilyCode(String familyCode);
}
