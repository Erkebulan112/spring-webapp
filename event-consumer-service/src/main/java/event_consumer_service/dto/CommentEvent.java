package event_consumer_service.dto;

import java.time.LocalDateTime;

public record CommentEvent(
        String eventType,
        String title,
        String description,
        LocalDateTime createdAt
) { }
