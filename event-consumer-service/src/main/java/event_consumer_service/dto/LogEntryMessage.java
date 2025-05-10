package event_consumer_service.dto;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record LogEntryMessage(
        Object id,
        String eventType,
        String entityId,
        String entityType,
        Map<String, Object> context,
        LocalDateTime timestamp
) { }