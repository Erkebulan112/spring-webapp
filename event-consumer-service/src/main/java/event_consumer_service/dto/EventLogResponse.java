package event_consumer_service.dto;

import event_consumer_service.entity.EventLog;
import lombok.Builder;
import org.bson.types.ObjectId;
import java.util.Map;

@Builder
public record EventLogResponse(
        String id,
        String eventType,
        String entityId,
        String entityType,
        Map<String, Object> payload
) {
    public static EventLogResponse toDto(EventLog eventLog) {
        return new EventLogResponse(
                eventLog.getId().toHexString(),
                eventLog.getEventType(),
                eventLog.getEntityId(),
                eventLog.getEntityType(),
                eventLog.getPayload()
        );
    }

    public EventLog toEntity() {
        EventLog eventLog = new EventLog();
        eventLog.setId(new ObjectId(id));
        eventLog.setEventType(eventType);
        eventLog.setEntityId(entityId);
        eventLog.setEntityType(entityType);
        eventLog.setPayload(payload);

        return eventLog;
    }
}


