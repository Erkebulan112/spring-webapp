package myrzakhan_taskflow.dtos.event;

import myrzakhan_taskflow.entities.elastic.ProjectIndex;
import myrzakhan_taskflow.entities.enums.ProjectStatus;
import myrzakhan_taskflow.entities.postgres.Project;

public record ProjectIndexEvent(
        Long id,
        String name,
        String description,
        ProjectStatus status
) implements IndexingEvent {
    public static ProjectIndexEvent toDto(Project project) {
        return new ProjectIndexEvent(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus()
        );
    }

    public ProjectIndex toIndex() {
        ProjectIndex projectIndex = new ProjectIndex();
        projectIndex.setId(id);
        projectIndex.setName(name);
        projectIndex.setDescription(description);
        projectIndex.setStatus(status);
        return projectIndex;
    }
}

