package event_consumer_service.dto;

import java.time.LocalDateTime;

public record ArchiveProjectEvent(
        String title,
        String description,
        String status,
        LocalDateTime createdAt
) { }
