package myrzakhan_taskflow.dtos.requests;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;

import java.time.LocalDateTime;

@Builder
public record TaskUpdateRequest(

        String title,

        @Size(max = 500, message = "Description is too long")
        String description,

        TaskStatus status,

        Priority priority,

        LocalDateTime deadline) {
}
