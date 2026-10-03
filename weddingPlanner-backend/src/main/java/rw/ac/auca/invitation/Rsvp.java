package rw.ac.auca.invitation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.ac.auca.guest.RsvpStatus;

import java.time.Instant;

@Entity
@Table(name = "rsvps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rsvp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitation_id", nullable = false, unique = true)
    private Invitation invitation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RsvpStatus status;

    @Column(name = "plus_one_names", columnDefinition = "TEXT")
    private String plusOneNames;

    @Column(name = "dietary_preferences", columnDefinition = "TEXT")
    private String dietaryPreferences;

    @Column(name = "responded_at", nullable = false)
    private Instant respondedAt;

    @PrePersist
    protected void onCreate() {
        if (this.respondedAt == null) {
            this.respondedAt = Instant.now();
        }
    }
}
