package myrzakhan_taskflow.dtos.event;

import java.time.LocalDate;
import myrzakhan_taskflow.entities.elastic.TaskIndex;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import myrzakhan_taskflow.entities.postgres.Task;

public record TaskIndexEvent(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        LocalDate deadline,
        Long assignedUserId,
        Long projectId
) implements IndexingEvent {
    public static TaskIndexEvent toDto(Task task) {
        return new TaskIndexEvent(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDeadline() != null ? task.getDeadline().toLocalDate() : null,
                task.getUser() != null ? task.getUser().getId() : null,
                task.getProject() != null ? task.getProject().getId() : null
        );
    }

    public TaskIndex toIndex() {
        TaskIndex taskIndex = new TaskIndex();
        taskIndex.setId(id);
        taskIndex.setTitle(title);
        taskIndex.setDescription(description);
        taskIndex.setStatus(status);
        taskIndex.setPriority(priority);
        taskIndex.setDeadline(deadline);
        taskIndex.setAssignedUserId(assignedUserId);
        taskIndex.setProjectId(projectId);
        return taskIndex;
    }
}
