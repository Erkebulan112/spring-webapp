package notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record NotificationEvent(
        Long taskId,

        @NotBlank(message = "Title cannot be null")
        @Size(min = 1, max = 255, message = "Title is too long") String message,
        String title,

        @NotBlank(message = "Status cannot be null")
        String status
) {
}
