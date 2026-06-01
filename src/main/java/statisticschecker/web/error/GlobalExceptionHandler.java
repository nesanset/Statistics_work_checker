package statisticschecker.web.error;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String message = "Некорректные данные запроса";
        if (!exception.getFieldErrors().isEmpty()) {
            message = exception.getFieldErrors().get(0).getDefaultMessage();
        }
        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableRequestBody() {
        return buildResponse("Некорректное тело запроса", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(message, LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }
}
