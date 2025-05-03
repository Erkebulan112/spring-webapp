package notification_service.dto;

import lombok.Builder;

@Builder
public record NotificationEvent(
        Long taskId,
        String title,
        String status
) {
}
