package notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.*;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String TASKFLOW_DIRECT_EXCHANGE = "taskflow.direct.exchange";
    public static final String TASKFLOW_FANOUT_EXCHANGE = "taskflow.fanout.exchange";
    public static final String TASKFLOW_TOPIC_EXCHANGE = "taskflow.topic.exchange";
    public static final String TASKFLOW_HEADERS_EXCHANGE = "taskflow.headers.exchange";
    public static final String TASKFLOW_DLX_EXCHANGE = "taskflow.dlx.exchange";

    public static final String TASK_NOTIFICATIONS_QUEUE = "task.notifications";
    public static final String TASK_AUDIT_FANOUT_QUEUE = "task.audit.fanout";
    public static final String TASK_NOTIFICATIONS_TOPIC_QUEUE = "task.notifications.topic";
    public static final String TASK_ERROR_TOPIC_QUEUE = "task.error.topic";
    public static final String TASK_NOTIFICATIONS_HEADERS_QUEUE = "task.notifications.headers";
    public static final String TASK_DLX_NOTIFICATIONS_QUEUE = "task.dlx.notifications";

    public static final String TASK_NOTIFICATION_DIRECT_RK = "notification.routing.key";
    public static final String TASK_NOTIFICATION_TOPIC_RK = "task.notification.*";
    public static final String TASK_NOTIFICATION_TOPIC_ERROR_RK = "task.error.#";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(TASKFLOW_DIRECT_EXCHANGE);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(TASKFLOW_FANOUT_EXCHANGE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TASKFLOW_TOPIC_EXCHANGE);
    }

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(TASKFLOW_HEADERS_EXCHANGE);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(TASKFLOW_DLX_EXCHANGE);
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable(TASK_NOTIFICATIONS_QUEUE)
                .withArgument("x-dead-letter-exchange", TASKFLOW_DLX_EXCHANGE)
                .withArgument("x-dead-letter-queue", TASK_DLX_NOTIFICATIONS_QUEUE)
                .build();
    }

    @Bean
    public Queue auditFanoutQueue() {
        return new Queue(TASK_AUDIT_FANOUT_QUEUE);
    }

    @Bean
    public Queue notificationsTopicQueue() {
        return new Queue(TASK_NOTIFICATIONS_TOPIC_QUEUE);
    }

    @Bean
    public Queue errorTopicQueue() {
        return new Queue(TASK_ERROR_TOPIC_QUEUE);
    }

    @Bean
    public Queue notificationsHeadersQueue() {
        return new Queue(TASK_NOTIFICATIONS_HEADERS_QUEUE);
    }

    @Bean
    public Queue dlxNotificationsQueue() {
        return new Queue(TASK_DLX_NOTIFICATIONS_QUEUE);
    }

    @Bean
    public Binding notificationsBinding() {
        return BindingBuilder
                .bind(notificationsQueue())
                .to(directExchange())
                .with(TASK_NOTIFICATION_DIRECT_RK);
    }

    @Bean
    public Binding auditFanoutBinding() {
        return BindingBuilder
                .bind(auditFanoutQueue())
                .to(fanoutExchange());
    }

    @Bean
    public Binding topicNotificationsBinding() {
        return BindingBuilder
                .bind(notificationsTopicQueue())
                .to(topicExchange())
                .with(TASK_NOTIFICATION_TOPIC_RK);
    }

    @Bean
    public Binding errorTopicNotificationsBinding() {
        return BindingBuilder
                .bind(notificationsHeadersQueue())
                .to(topicExchange())
                .with(TASK_NOTIFICATION_TOPIC_ERROR_RK);
    }

    @Bean
    public Binding notificationsHeadersBinding() {
        return BindingBuilder
                .bind(notificationsHeadersQueue())
                .to(headersExchange())
                .whereAll(Map.of("type", "notification",
                        "format", "json"))
                .match();
    }

    @Bean
    public Binding dlxNotificationsBinding() {
        return BindingBuilder
                .bind(dlxNotificationsQueue())
                .to(dlxExchange())
                .with(TASK_DLX_NOTIFICATIONS_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
