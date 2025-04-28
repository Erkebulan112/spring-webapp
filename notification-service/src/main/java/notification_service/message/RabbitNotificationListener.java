package notification_service.message;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification_service.config.RabbitMQConfig;
import notification_service.dto.NotificationEvent;
import notification_service.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitNotificationListener {

    private final NotificationService notificationService;
    private final RetryTemplate retryTemplate;

    private static final int MAX_RETRIES = 5;

    @RabbitListener(queues = RabbitMQConfig.TASK_NOTIFICATIONS_QUEUE)
    public void receive(NotificationEvent event, Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                        @Header(value = "x-death", required = false) List<Map<String, Object>> xDeath) {

        int retries = xDeath != null ? ((Long) xDeath.get(0).getOrDefault("count", 0L)).intValue() : 0;

        try {
            retryTemplate.execute(ctx -> {
                notificationService.save(event);
                return null;
            });
            channel.basicAck(tag, false);
        } catch (Exception e) {
            handleFailure(channel, tag, retries);
        }
    }

    private void handleFailure(Channel channel, long tag, int retries) {
        try {
            if (retries >= MAX_RETRIES) {
                channel.basicNack(tag, false, false);
            } else {
                channel.basicNack(tag, false, true);
            }
        } catch (IOException e) {
            log.error("Nack error", e);
        }
    }
}
