package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.Task;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import java.time.LocalDateTime;

public record TaskCreateResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        LocalDateTime deadline)
{
    public static TaskCreateResponse toDto(Task task) {
        return new TaskCreateResponse(task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(), task.getDeadline());
    }
}
