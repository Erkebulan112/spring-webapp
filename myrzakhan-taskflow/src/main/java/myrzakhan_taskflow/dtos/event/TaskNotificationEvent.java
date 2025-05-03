package myrzakhan_taskflow.dtos.event;

import lombok.Builder;

@Builder
public record TaskNotificationEvent(
        Long taskId,
        String title,
        NotificationStatus status
) { }
