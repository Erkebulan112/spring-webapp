package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.Project;
import myrzakhan_taskflow.entities.enums.ProjectStatus;
import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
{
    public static ProjectResponse toDto(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(),
                project.getStatus(), project.getCreatedAt(), project.getUpdatedAt());
    }
}
