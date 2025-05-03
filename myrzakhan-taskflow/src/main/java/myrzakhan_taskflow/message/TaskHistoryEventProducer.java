package myrzakhan_taskflow.message;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.dtos.event.TaskHistoryEvent;
import myrzakhan_taskflow.dtos.event.TaskNotificationEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskHistoryEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendTaskHistoryEvent(String exchange, String routingKey, TaskHistoryEvent taskHistoryEvent) {
        rabbitTemplate.convertAndSend(exchange, routingKey, taskHistoryEvent);
    }

    public void sendTaskHistoryToNotification(String exchange, String routingKey, TaskNotificationEvent notificationEvent) {
        rabbitTemplate.convertAndSend(exchange, routingKey, notificationEvent);
    }
}
