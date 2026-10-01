package rw.ac.auca.ceremony;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.ac.auca.base.BaseEntity;
import rw.ac.auca.wedding.Wedding;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "wedding_ceremonies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeddingCeremony extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private Wedding wedding;

    @Enumerated(EnumType.STRING)
    @Column(name = "ceremony_type", nullable = false, length = 30)
    private CeremonyType ceremonyType;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "ceremony_date")
    private LocalDate ceremonyDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "venue_location", length = 255)
    private String venueLocation;

    @Column(columnDefinition = "TEXT")
    private String description;
}
