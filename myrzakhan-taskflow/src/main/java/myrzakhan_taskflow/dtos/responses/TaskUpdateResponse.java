package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import myrzakhan_taskflow.entities.postgres.Task;
import java.time.LocalDateTime;

public record TaskUpdateResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        LocalDateTime deadline,
        LocalDateTime updatedAt)
{
    public static TaskUpdateResponse toDto(Task task) {
        return new TaskUpdateResponse(task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(), task.getDeadline(), task.getUpdatedAt());
    }
}
