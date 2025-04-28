package myrzakhan_taskflow.controllers.mongo;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.dtos.responses.TaskHistoryResponse;
import myrzakhan_taskflow.services.mongo.TaskHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    @GetMapping("/{taskId}/history")
    public ResponseEntity<List<TaskHistoryResponse>> getTaskHistory(@PathVariable Long taskId) {
        var responses = taskHistoryService.findTaskHistory(taskId)
                .stream().map(TaskHistoryResponse::toDto).toList();
        return ResponseEntity.ok(responses);
    }
}
