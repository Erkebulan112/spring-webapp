package myrzakhan_taskflow.services.postgres;

import myrzakhan_taskflow.dtos.requests.ProjectCreateRequest;
import myrzakhan_taskflow.entities.enums.ProjectStatus;
import myrzakhan_taskflow.entities.postgres.Project;
import myrzakhan_taskflow.repositories.postgres.ProjectRepository;
import myrzakhan_taskflow.services.postgres.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private ProjectCreateRequest projectRequest;

    @BeforeEach
    void setUp() {

        projectRequest = new ProjectCreateRequest("Project", "Create a new database", ProjectStatus.ACTIVE);

        project = new Project();
        project.setName(projectRequest.name());
        project.setDescription(projectRequest.description());
        project.setStatus(projectRequest.status());

    }

    @Test
    void testCreateProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        var response = projectService.createProject(projectRequest);

        assertAll("Testing createProject method",
                () -> assertNotNull(response, "Response is null"),
                () -> assertEquals(project.getName(), response.getName(), "Name does not match"),
                () -> assertEquals(project, response, "Objects does not match"),
                () -> verify(projectRepository, times(1)).save(any(Project.class)));
    }

    @Test
    void testDeleteProject() {
        doNothing().when(projectRepository).deleteById(anyLong());
        projectService.deleteProject(anyLong());
        verify(projectRepository, times(1)).deleteById(anyLong());
    }
}
