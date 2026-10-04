package rw.ac.auca.timeline;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.wedding.Wedding;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "timeline_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private Wedding wedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ceremony_id")
    private WeddingCeremony ceremony;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Column(name = "target_time")
    private LocalTime targetTime;

    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private boolean completed = false;
}
