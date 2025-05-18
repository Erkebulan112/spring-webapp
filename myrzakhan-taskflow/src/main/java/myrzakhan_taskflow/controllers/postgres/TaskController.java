package myrzakhan_taskflow.controllers.postgres;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.controllers.PageableConstants;
import myrzakhan_taskflow.dtos.requests.TaskCreateRequest;
import myrzakhan_taskflow.dtos.requests.TaskUpdateRequest;
import myrzakhan_taskflow.dtos.responses.TaskCreateResponse;
import myrzakhan_taskflow.dtos.responses.TaskResponse;
import myrzakhan_taskflow.dtos.responses.TaskSearchResponse;
import myrzakhan_taskflow.dtos.responses.TaskUpdateResponse;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import myrzakhan_taskflow.services.postgres.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @PageableDefault(
                    size = PageableConstants.DEFAULT_SIZE,
                    page = PageableConstants.DEFAULT_PAGE,
                    sort = PageableConstants.DEFAULT_SORT_BY,
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        var responses = taskService.findAllTasks(pageable)
                .map(TaskResponse::toDto);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        var response = taskService.findTaskById(id);
        return ResponseEntity.ok(TaskResponse.toDto(response));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskSearchResponse>> searchTasks(
            @RequestParam String query,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority) {

        var tasks = taskService.searchTasks(query, status, priority);

        var result = tasks.stream()
                .map(TaskSearchResponse::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<TaskCreateResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        var response = taskService.createTask(request);
        return ResponseEntity.created(URI.create("/tasks/")).body(TaskCreateResponse.toDto(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskUpdateResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request) {
        var response = taskService.updateTask(id, request);
        return ResponseEntity.ok(TaskUpdateResponse.toDto(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
