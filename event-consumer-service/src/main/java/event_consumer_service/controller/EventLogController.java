package event_consumer_service.controller;

import event_consumer_service.dto.EventLogResponse;
import event_consumer_service.service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proessed-events")
@RequiredArgsConstructor
public class EventLogController {

    private final EventLogService eventLogService;

    @GetMapping
    public ResponseEntity<Page<EventLogResponse>> getAll(Pageable pageable) {
        var responses = eventLogService.getAll(pageable).map(EventLogResponse::toDto);
        return ResponseEntity.ok(responses);
    }
}
