package myrzakhan_taskflow.dtos.responses;

import lombok.Builder;
import myrzakhan_taskflow.dtos.event.LogLevel;
import myrzakhan_taskflow.entities.mongo.LogEntry;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record LogEntryResponse(
        ObjectId id,
        LogLevel level,
        String message,
        Map<String, Object> context,
        LocalDateTime timestamp)
{
    public static LogEntryResponse toDto(LogEntry log) {
        return new LogEntryResponse(log.getId(), log.getLevel(),
                log.getMessage(), log.getContext(), log.getTimestamp());
    }
}
