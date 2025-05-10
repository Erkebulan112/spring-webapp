package myrzakhan_taskflow.message;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.config.KafkaConfig;
import myrzakhan_taskflow.dtos.responses.LogEntryResponse;
import myrzakhan_taskflow.services.mongo.LogEntryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaLogConsumer {

    private final LogEntryService logEntryService;

    @KafkaListener(topics = KafkaConfig.TASKFLOW_LOG_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveLogEvents(LogEntryResponse response) {
        logEntryService.createLogs(response.level(), response.message(), response.context());
    }
}