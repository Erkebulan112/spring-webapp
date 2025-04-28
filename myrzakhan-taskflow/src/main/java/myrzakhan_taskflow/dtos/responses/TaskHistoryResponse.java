package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.mongo.TaskHistory;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.Map;

public record TaskHistoryResponse(
        ObjectId id,
        Long taskId,
        String action,
        Long performedBy,
        LocalDateTime timestamp,
        Map<String, Object> details)
{
    public static TaskHistoryResponse toDto(TaskHistory task) {
        return new TaskHistoryResponse(task.getId(), task.getTaskId(), task.getAction(),
                task.getPerformedBy(), task.getTimestamp(), task.getDetails());
    }
}
