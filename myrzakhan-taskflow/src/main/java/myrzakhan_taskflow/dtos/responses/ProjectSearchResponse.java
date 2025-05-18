package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.elastic.ProjectIndex;
import myrzakhan_taskflow.entities.enums.ProjectStatus;

public record ProjectSearchResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status
) {
    public static ProjectSearchResponse toDto(ProjectIndex projectIndex) {
        return new ProjectSearchResponse(projectIndex.getId(), projectIndex.getName(),
                projectIndex.getDescription(), projectIndex.getStatus());
    }
}
