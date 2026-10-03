package rw.ac.auca.seating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestSeatingRepository extends JpaRepository<GuestSeating, Long> {

    List<GuestSeating> findByTableId(Long tableId);

    Optional<GuestSeating> findByGuestId(Long guestId);

    boolean existsByGuestId(Long guestId);

    void deleteByGuestId(Long guestId);

    void deleteByTableId(Long tableId);

    List<GuestSeating> findByGuestIdIn(List<Long> guestIds);
}
