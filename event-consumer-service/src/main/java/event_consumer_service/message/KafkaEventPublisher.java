package event_consumer_service.message;

import event_consumer_service.config.KafkaConfig;
import event_consumer_service.entity.EventLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendToDLQ(EventLog eventLog) {
        log.info("Sending event to Kafka : {}", eventLog);
        kafkaTemplate.send(KafkaConfig.TASKFLOW_DLQ_TOPIC, eventLog.getEntityId(), eventLog);
    }
}
