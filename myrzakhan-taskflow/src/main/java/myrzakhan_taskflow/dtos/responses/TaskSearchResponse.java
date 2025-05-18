package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.elastic.TaskIndex;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import java.time.LocalDate;

public record TaskSearchResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        LocalDate deadline,
        Long assignedUserId,
        Long projectId
) {
    public static TaskSearchResponse toDto(TaskIndex taskIndex) {
        return new TaskSearchResponse(taskIndex.getId(), taskIndex.getTitle(), taskIndex.getDescription(), taskIndex.getStatus(),
                taskIndex.getPriority(), taskIndex.getDeadline(), taskIndex.getAssignedUserId(), taskIndex.getProjectId());
    }
}
