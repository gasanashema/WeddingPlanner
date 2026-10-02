package rw.ac.auca.nosql.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogDocument {

    @Id
    private String id;
    private Long weddingId;
    private String userEmail;
    private String activityType; // TASK_CREATED, EXPENSE_ADDED, GUEST_RSVP, INVITATION_VIEWED
    private String description;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
