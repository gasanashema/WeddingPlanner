package rw.ac.auca.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "wedding.invitations.exchange";
    public static final String DLX_NAME = "wedding.dlx";

    public static final String EMAIL_QUEUE = "wedding.invitations.email";
    public static final String SMS_QUEUE = "wedding.invitations.sms";
    public static final String RSVP_QUEUE = "wedding.rsvp.notifications";
    public static final String DEAD_LETTER_QUEUE = "wedding.dead-letter";

    public static final String EMAIL_ROUTING_KEY = "wedding.invitation.email";
    public static final String SMS_ROUTING_KEY = "wedding.invitation.sms";
    public static final String RSVP_ROUTING_KEY = "wedding.rsvp.notification";
    public static final String DLX_ROUTING_KEY = "wedding.dead-letter.key";

    @Bean
    public TopicExchange weddingExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_NAME);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLX_ROUTING_KEY);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(SMS_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue rsvpQueue() {
        return QueueBuilder.durable(RSVP_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange weddingExchange) {
        return BindingBuilder.bind(emailQueue).to(weddingExchange).with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding smsBinding(Queue smsQueue, TopicExchange weddingExchange) {
        return BindingBuilder.bind(smsQueue).to(weddingExchange).with(SMS_ROUTING_KEY);
    }

    @Bean
    public Binding rsvpBinding(Queue rsvpQueue, TopicExchange weddingExchange) {
        return BindingBuilder.bind(rsvpQueue).to(weddingExchange).with(RSVP_ROUTING_KEY);
    }
}
