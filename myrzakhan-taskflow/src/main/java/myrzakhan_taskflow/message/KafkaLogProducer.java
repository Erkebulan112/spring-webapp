package myrzakhan_taskflow.message;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.config.KafkaConfig;
import myrzakhan_taskflow.dtos.event.CommentIndexDelete;
import myrzakhan_taskflow.dtos.event.CommentIndexEvent;
import myrzakhan_taskflow.dtos.event.IndexingEvent;
import myrzakhan_taskflow.dtos.event.LogEntryMessage;
import myrzakhan_taskflow.dtos.event.LogLevel;
import myrzakhan_taskflow.dtos.event.ProjectIndexDelete;
import myrzakhan_taskflow.dtos.event.ProjectIndexEvent;
import myrzakhan_taskflow.dtos.event.TaskIndexDelete;
import myrzakhan_taskflow.dtos.event.TaskIndexEvent;
import myrzakhan_taskflow.dtos.responses.LogEntryResponse;
import myrzakhan_taskflow.entities.postgres.Task;
import org.bson.types.ObjectId;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    public void sendIndexEvent(IndexingEvent event) {
        var topic = resolveTopic(event);
        kafkaTemplate.send(topic, event);
        log.info("Sent indexing event to topic [{}]: {}", topic, event);
    }

    public String resolveTopic(IndexingEvent event) {
        if (event instanceof CommentIndexEvent || event instanceof CommentIndexDelete) return KafkaConfig.INDEX_COMMENT_TOPIC;
        if (event instanceof TaskIndexEvent || event instanceof TaskIndexDelete) return KafkaConfig.INDEX_TASK_TOPIC;
        if (event instanceof ProjectIndexEvent || event instanceof ProjectIndexDelete) return KafkaConfig.INDEX_PROJECT_TOPIC;
        throw new IllegalArgumentException("Unknown event type: " + event.getClass().getName());
    }
}
