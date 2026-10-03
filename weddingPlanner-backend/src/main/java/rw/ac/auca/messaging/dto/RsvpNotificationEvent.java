package rw.ac.auca.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.guest.RsvpStatus;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsvpNotificationEvent implements Serializable {

    private Long weddingId;
    private String guestName;
    private RsvpStatus status;
    private String plusOneNames;
}
