package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.enums.ProjectStatus;
import myrzakhan_taskflow.entities.postgres.Project;

public record ProjectCreateResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status)
{
    public static ProjectCreateResponse toDto(Project project) {
        return new ProjectCreateResponse(project.getId(), project.getName(), project.getDescription(),
                project.getStatus());
    }
}
