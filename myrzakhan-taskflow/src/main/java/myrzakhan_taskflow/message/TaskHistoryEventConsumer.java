package myrzakhan_taskflow.message;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.config.RabbitMQConfig;
import myrzakhan_taskflow.dtos.event.TaskHistoryEvent;
import myrzakhan_taskflow.services.mongo.TaskHistoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskHistoryEventConsumer {

    private final TaskHistoryService taskHistoryService;

    @RabbitListener(queues = RabbitMQConfig.TASK_HISTORY_QUEUE)
    public void receiveTaskHistoryEvent(TaskHistoryEvent event) {
        taskHistoryService.save(
                event.taskId(),
                event.action(),
                event.performedBy(),
                event.details()
        );
    }
}
