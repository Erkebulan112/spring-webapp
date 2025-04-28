package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.mongo.LogEntry;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.Map;

public record LogEntryResponse(
        ObjectId id,
        String level,
        String message,
        LocalDateTime timestamp,
        Map<String, Object> context)
{
    public static LogEntryResponse toDto(LogEntry log) {
        return new LogEntryResponse(log.getId(), log.getLevel(),
                log.getMessage(), log.getTimestamp(), log.getContext());
    }
}
