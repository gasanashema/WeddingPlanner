package rw.ac.auca.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findByWeddingId(Long weddingId);

    Optional<Vendor> findByIdAndWeddingId(Long id, Long weddingId);
}
