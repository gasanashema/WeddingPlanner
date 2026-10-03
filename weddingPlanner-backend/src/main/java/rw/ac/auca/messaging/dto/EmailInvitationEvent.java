package rw.ac.auca.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailInvitationEvent implements Serializable {

    private String invitationToken;
    private String guestEmail;
    private String guestName;
    private String weddingTitle;
    private String shareableUrl;
}
