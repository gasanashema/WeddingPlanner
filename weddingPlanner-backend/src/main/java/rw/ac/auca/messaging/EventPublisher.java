package rw.ac.auca.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import rw.ac.auca.config.RabbitMQConfig;
import rw.ac.auca.messaging.dto.EmailInvitationEvent;
import rw.ac.auca.messaging.dto.RsvpNotificationEvent;
import rw.ac.auca.messaging.dto.SmsNotificationEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishEmailInvitation(EmailInvitationEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.EMAIL_ROUTING_KEY, event);
            log.info("Published EmailInvitationEvent to RabbitMQ for guest: {}", event.getGuestName());
        } catch (Exception e) {
            log.warn("Failed to publish EmailInvitationEvent to RabbitMQ broker (local fallback active): {}", e.getMessage());
        }
    }

    public void publishSmsNotification(SmsNotificationEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.SMS_ROUTING_KEY, event);
            log.info("Published SmsNotificationEvent to RabbitMQ for phone: {}", event.getGuestPhone());
        } catch (Exception e) {
            log.warn("Failed to publish SmsNotificationEvent to RabbitMQ broker: {}", e.getMessage());
        }
    }

    public void publishRsvpNotification(RsvpNotificationEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RSVP_ROUTING_KEY, event);
            log.info("Published RsvpNotificationEvent to RabbitMQ for guest: {} [{}]", event.getGuestName(), event.getStatus());
        } catch (Exception e) {
            log.warn("Failed to publish RsvpNotificationEvent to RabbitMQ broker: {}", e.getMessage());
        }
    }
}
