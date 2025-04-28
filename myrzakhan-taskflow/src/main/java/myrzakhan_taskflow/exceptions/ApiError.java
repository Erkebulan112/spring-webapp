package myrzakhan_taskflow.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Builder
public record ApiError(
        HttpStatus status,
        String message,
        String error,
        LocalDateTime timestamp)
{
    public ApiError(HttpStatus status, String message, String error) {
        this(status, message, error, LocalDateTime.now());
    }
}
