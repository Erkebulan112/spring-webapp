package myrzakhan_taskflow.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(

        @NotBlank
        String content,

        @NotNull(message = "Creating comment without user is not allowed")
        Long userId
) { }
