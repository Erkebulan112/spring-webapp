package myrzakhan_taskflow.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import myrzakhan_taskflow.entities.enums.ProjectStatus;

public record ProjectUpdateRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Size(max = 500, message = "Description is too long")
        String description,

        ProjectStatus status) {
}
