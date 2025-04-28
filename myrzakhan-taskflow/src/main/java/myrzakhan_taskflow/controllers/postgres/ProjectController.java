package myrzakhan_taskflow.controllers.postgres;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.controllers.PageableConstants;
import myrzakhan_taskflow.dtos.requests.ProjectCreateRequest;
import myrzakhan_taskflow.dtos.requests.ProjectUpdateRequest;
import myrzakhan_taskflow.dtos.responses.ProjectCreateResponse;
import myrzakhan_taskflow.dtos.responses.ProjectResponse;
import myrzakhan_taskflow.dtos.responses.ProjectUpdateResponse;
import myrzakhan_taskflow.services.postgres.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @PageableDefault(
                    size = PageableConstants.DEFAULT_SIZE,
                    page = PageableConstants.DEFAULT_PAGE,
                    sort = PageableConstants.DEFAULT_SORT_BY,
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        var responses = projectService.findAllProjects(pageable)
                .map(ProjectResponse::toDto);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        var response = projectService.findProjectById(id);
        return ResponseEntity.ok(ProjectResponse.toDto(response));
    }

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        var response = projectService.createProject(request);
        return ResponseEntity.created(URI.create("/projects/")).body(ProjectCreateResponse.toDto(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectUpdateResponse> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectUpdateRequest request) {
        var response = projectService.updateProject(id, request);
        return ResponseEntity.ok(ProjectUpdateResponse.toDto(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
