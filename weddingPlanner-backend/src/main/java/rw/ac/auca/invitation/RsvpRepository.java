package rw.ac.auca.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, Long> {

    Optional<Rsvp> findByInvitationId(Long invitationId);
}
