package rw.ac.auca.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rw.ac.auca.config.RabbitMQConfig;
import rw.ac.auca.messaging.dto.EmailInvitationEvent;
import rw.ac.auca.messaging.dto.RsvpNotificationEvent;
import rw.ac.auca.messaging.dto.SmsNotificationEvent;

@Component
@Slf4j
public class EventListener {

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmailInvitation(EmailInvitationEvent event) {
        log.info("[RabbitMQ Email Consumer] Dispatching invitation email to {} ({}) for wedding: {}",
                event.getGuestName(), event.getGuestEmail(), event.getWeddingTitle());
        // Simulates async email provider dispatch (SendGrid / Mailgun)
    }

    @RabbitListener(queues = RabbitMQConfig.SMS_QUEUE)
    public void handleSmsNotification(SmsNotificationEvent event) {
        log.info("[RabbitMQ SMS Consumer] Sending SMS notification to {}: {}",
                event.getGuestPhone(), event.getMessage());
        // Simulates async SMS provider dispatch (Twilio / Africa's Talking)
    }

    @RabbitListener(queues = RabbitMQConfig.RSVP_QUEUE)
    public void handleRsvpNotification(RsvpNotificationEvent event) {
        log.info("[RabbitMQ RSVP Consumer] Processed RSVP notification for weddingId {}: Guest {} status updated to {}",
                event.getWeddingId(), event.getGuestName(), event.getStatus());
        // Simulates async push notification / audit logging feed
    }
}
