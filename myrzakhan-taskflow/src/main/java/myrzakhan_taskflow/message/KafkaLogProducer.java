package myrzakhan_taskflow.message;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.config.KafkaConfig;
import myrzakhan_taskflow.dtos.event.LogEntryMessage;
import myrzakhan_taskflow.dtos.event.LogLevel;
import myrzakhan_taskflow.dtos.responses.LogEntryResponse;
import myrzakhan_taskflow.entities.postgres.Task;
import org.bson.types.ObjectId;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaLogProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendLog(LogLevel level, String message, Map<String, Object> context) {
        LogEntryResponse log = LogEntryResponse.builder()
                .level(level)
                .message(message)
                .context(context)
                .build();

        kafkaTemplate.send(KafkaConfig.TASKFLOW_LOG_TOPIC, log);
    }

    public void sendTaskLog(Task task, String action) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", task.getTitle());
        payload.put("description", task.getDescription());
        payload.put("status", task.getStatus());

        LogEntryMessage taskLogMessage = new LogEntryMessage(
                new ObjectId().toHexString(),
                action,
                String.valueOf(task.getId()),
                task.getClass().getSimpleName(),
                payload
        );

        kafkaTemplate.send(KafkaConfig.TASKFLOW_EVENTS_TOPIC, taskLogMessage);
    }
}
