package myrzakhan_taskflow.dtos.event;

import lombok.Builder;
import java.util.Map;

@Builder
public record LogEntryMessage(
        String id,
        String eventType,
        String entityId,
        String entityType,
        Map<String, Object> payload
) { }