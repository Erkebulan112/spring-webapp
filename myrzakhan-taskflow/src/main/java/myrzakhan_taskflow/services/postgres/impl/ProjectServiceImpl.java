package myrzakhan_taskflow.services.postgres.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.dtos.requests.ProjectCreateRequest;
import myrzakhan_taskflow.dtos.requests.ProjectUpdateRequest;
import myrzakhan_taskflow.entities.postgres.Project;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.repositories.postgres.ProjectRepository;
import myrzakhan_taskflow.services.postgres.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAllProjects(Pageable pageable) {
        log.info("Get all projects");
        return projectRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Project findProjectById(Long id) {
        log.info("Get project by id: {}", id);
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project not found with id: %d".formatted(id)));
    }

    @Override
    public Project createProject(ProjectCreateRequest request) {
        log.info("Create project: {}", request);
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(request.status());
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, ProjectUpdateRequest request) {
        log.info("Update project: {}", request);
        var project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project not found with id: %d".formatted(id)));
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(request.status());
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        log.info("Delete project: {}", id);
        projectRepository.deleteById(id);
    }
}
