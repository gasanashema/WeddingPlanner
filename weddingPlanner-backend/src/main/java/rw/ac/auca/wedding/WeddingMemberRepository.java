package rw.ac.auca.wedding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeddingMemberRepository extends JpaRepository<WeddingMember, Long> {

    List<WeddingMember> findByWeddingId(Long weddingId);

    List<WeddingMember> findByUserId(Long userId);

    Optional<WeddingMember> findByWeddingIdAndUserId(Long weddingId, Long userId);

    @Query("SELECT COUNT(m) FROM WeddingMember m WHERE m.wedding.id = :weddingId AND m.side = :side AND (m.role LIKE '%SUPPORT%' OR m.role LIKE '%SUPPORT')")
    long countSupportMembersByWeddingAndSide(@Param("weddingId") Long weddingId, @Param("side") WeddingSide side);
}
