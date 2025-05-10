package event_consumer_service.message;

import event_consumer_service.config.KafkaConfig;
import event_consumer_service.dto.EventLogResponse;
import event_consumer_service.entity.EventLog;
import event_consumer_service.service.EventLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final EventLogService eventLogService;
    private final KafkaEventPublisher kafkaEventPublisher;

    @KafkaListener(topics = KafkaConfig.TASKFLOW_EVENTS_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveLogEntryMessage(EventLogResponse message, Acknowledgment ack) {
        try {
            log.info("Received EVENT: {}", message);
            EventLog eventLog = new EventLog();
            eventLog.setEventType(message.eventType());
            eventLog.setEntityId(message.entityId());
            eventLog.setEntityType(message.entityType());
            eventLog.setPayload(message.payload());
            eventLogService.save(eventLog);

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process log message: {}", message, e);

            var eventLog = message.toEntity();
            kafkaEventPublisher.sendToDLQ(eventLog);
            ack.acknowledge();
        }
    }

    // Метод для преобразования Payload в Map
//    private Map<String, Object> payloadToMap(Payload payload) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("title", payload.title());
//        map.put("description", payload.description());
//        map.put("status", payload.status());
//        return map;
//    }
//    public void receiveEventLog(EventLog eventLog, Acknowledgment ack) {
//
//        try {
//            switch (eventLog.getEntityType()) {
//                case "TASK" -> {
//                    log.info("Received TASK event: {}", eventLog);
//                    eventLogService.save(eventLog);
//                }
//                case "COMMENT" -> {
//                    log.info("Received COMMENT event: {}", eventLog);
//                    kafkaEventPublisher.sendCommentNotification(new CommentEvent(
//                            eventLog.getEventType(),
//                            eventLog.getPayload().title(),
//                            eventLog.getPayload().description(),
//                            eventLog.getCreatedAt()
//                    ));
//                    eventLogService.save(eventLog);
//                }
//                case "PROJECT" -> {
//                    log.info("Received PROJECT event: {}", eventLog);
//                    kafkaEventPublisher.sendToProjectArchive(new ArchiveProjectEvent(
//                            eventLog.getPayload().title(),
//                            eventLog.getPayload().description(),
//                            eventLog.getPayload().status(),
//                            eventLog.getCreatedAt()
//                    ));
//                    eventLogService.save(eventLog);
//                }
//                default -> {
//                    log.warn("Unknown entityType: {}", eventLog.getEntityType());
//                }
//            }
//            ack.acknowledge();
//        } catch (Exception e) {
//            log.error("Failed to process event: {}", eventLog, e);
//            kafkaEventPublisher.sendToDLQ(eventLog);
//            ack.acknowledge();
//        }
//    }
}
