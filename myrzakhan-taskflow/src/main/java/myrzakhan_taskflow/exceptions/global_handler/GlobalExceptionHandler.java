package myrzakhan_taskflow.exceptions.global_handler;

import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.exceptions.ApiError;
import myrzakhan_taskflow.exceptions.FileSizeExceededException;
import myrzakhan_taskflow.exceptions.InvalidMimeTypeException;
import myrzakhan_taskflow.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        log.error("Not found exception: {}", e.getMessage());

        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({FileSizeExceededException.class, InvalidMimeTypeException.class})
    public ResponseEntity<ApiError> handleFileValidationExceptions(RuntimeException e) {
        log.error("Validation error: {} - {}", e.getClass().getSimpleName(), e.getMessage());

        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
