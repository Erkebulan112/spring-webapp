package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.enums.ProjectStatus;
import myrzakhan_taskflow.entities.postgres.Project;
import java.time.LocalDateTime;

public record ProjectUpdateResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status,
        LocalDateTime updatedAt)
{
    public static ProjectUpdateResponse toDto(Project project) {
        return new ProjectUpdateResponse(project.getId(), project.getName(), project.getDescription(),
                project.getStatus(), project.getUpdatedAt());
    }
}
