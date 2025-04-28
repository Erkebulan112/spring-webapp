package myrzakhan_taskflow.exception_handler;

import myrzakhan_taskflow.exceptions.ApiError;
import myrzakhan_taskflow.exceptions.FileSizeExceededException;
import myrzakhan_taskflow.exceptions.InvalidMimeTypeException;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.exceptions.global_handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException notFoundException = new NotFoundException("Not found");

        ResponseEntity<ApiError> response = globalExceptionHandler.handleNotFoundException(notFoundException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().message());
        assertEquals("Not Found", response.getBody().error());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void testHandleFileValidationExceptions() {
        FileSizeExceededException fileSizeExceededException = new FileSizeExceededException("File Size Exceeded");

        ResponseEntity<ApiError> response = globalExceptionHandler.handleFileValidationExceptions(fileSizeExceededException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("File Size Exceeded", response.getBody().message());
        assertEquals("Bad Request", response.getBody().error());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void testHandleInvalidMimeTypeException() {
        InvalidMimeTypeException invalidMimeTypeException = new InvalidMimeTypeException("Invalid MimeType");

        ResponseEntity<ApiError> response = globalExceptionHandler.handleFileValidationExceptions(invalidMimeTypeException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid MimeType", response.getBody().message());
        assertEquals("Bad Request", response.getBody().error());
        assertNotNull(response.getBody().timestamp());
    }
}
