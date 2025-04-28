package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.Task;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        LocalDateTime deadline,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
{
    public static TaskResponse toDto(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
                task.getPriority(), task.getDeadline(), task.getCreatedAt(), task.getUpdatedAt());
    }
}
