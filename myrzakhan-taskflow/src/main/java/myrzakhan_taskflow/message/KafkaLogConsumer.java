package myrzakhan_taskflow.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.config.KafkaConfig;
import myrzakhan_taskflow.dtos.event.CommentIndexDelete;
import myrzakhan_taskflow.dtos.event.CommentIndexEvent;
import myrzakhan_taskflow.dtos.event.ProjectIndexDelete;
import myrzakhan_taskflow.dtos.event.ProjectIndexEvent;
import myrzakhan_taskflow.dtos.event.TaskIndexDelete;
import myrzakhan_taskflow.dtos.event.TaskIndexEvent;
import myrzakhan_taskflow.dtos.responses.LogEntryResponse;
import myrzakhan_taskflow.repositories.elastic.CommentIndexRepository;
import myrzakhan_taskflow.repositories.elastic.ProjectIndexRepository;
import myrzakhan_taskflow.repositories.elastic.TaskIndexRepository;
import myrzakhan_taskflow.services.mongo.LogEntryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaLogConsumer {

    private final LogEntryService logEntryService;
    private final CommentIndexRepository commentIndexRepository;
    private final ProjectIndexRepository projectIndexRepository;
    private final TaskIndexRepository taskIndexRepository;

    @KafkaListener(topics = KafkaConfig.TASKFLOW_LOG_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveLogEvents(LogEntryResponse response) {
        logEntryService.createLogs(response.level(), response.message(), response.context());
    }

    @KafkaListener(topics = KafkaConfig.INDEX_COMMENT_TOPIC, groupId = "elastic_sync")
    public void listenCommentIndex(CommentIndexEvent event) {
        log.info("Received CommentIndexEvent: {}", event);
        commentIndexRepository.save(event.toIndex());
    }

    @KafkaListener(topics = KafkaConfig.INDEX_TASK_TOPIC, groupId = "elastic_sync")
    public void listenTaskIndex(TaskIndexEvent event) {
        log.info("Received TaskIndexEvent: {}", event);
        taskIndexRepository.save(event.toIndex());
    }

    @KafkaListener(topics = KafkaConfig.INDEX_PROJECT_TOPIC, groupId = "elastic_sync")
    public void listenProjectIndex(ProjectIndexEvent event) {
        log.info("Received ProjectIndexEvent: {}", event);
        projectIndexRepository.save(event.toIndex());
    }
}