package myrzakhan_taskflow.controllers.mongo;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.dtos.responses.LogEntryResponse;
import myrzakhan_taskflow.services.mongo.LogEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogEntryController {

    private final LogEntryService logEntryService;

    @GetMapping
    public ResponseEntity<List<LogEntryResponse>> getAllLogs() {
        var responses = logEntryService.getAll()
                .stream().map(LogEntryResponse::toDto).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/level")
    public ResponseEntity<List<LogEntryResponse>> getLogsByLevel(@RequestParam String level) {
        var response = logEntryService.getByLevel(level)
                .stream().map(LogEntryResponse::toDto).toList();
        return ResponseEntity.ok(response);
    }
}
