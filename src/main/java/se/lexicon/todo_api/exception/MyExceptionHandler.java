package se.lexicon.todo_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
// This annotation indicates that this class will handle exceptions globally for all controllers
public class MyExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        System.out.println("HandleNoResourceFoundException: " + ex.getMessage());
        String errorMessage = "Resource not found.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), new String[]{errorMessage});
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        System.out.println("RuntimeException: " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), new String[]{ex.getMessage()});
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        System.out.println("HandleGlobalException: " + ex.getMessage());
        String uuid = java.util.UUID.randomUUID().toString().toUpperCase();
        System.err.println("Error ID: " + uuid + " - " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new String[]{"An unexpected error occurred: " + uuid});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // todo: try to catch if validation exception happens
}
