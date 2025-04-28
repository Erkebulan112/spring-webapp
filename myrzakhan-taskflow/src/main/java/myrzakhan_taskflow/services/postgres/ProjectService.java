package myrzakhan_taskflow.services.postgres;

import myrzakhan_taskflow.dtos.requests.ProjectCreateRequest;
import myrzakhan_taskflow.dtos.requests.ProjectUpdateRequest;
import myrzakhan_taskflow.entities.postgres.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Page<Project> findAllProjects(Pageable pageable);

    Project findProjectById(Long id);

    Project createProject(ProjectCreateRequest project);

    Project updateProject(Long id, ProjectUpdateRequest project);

    void deleteProject(Long id);
}
