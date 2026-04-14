package com.digitalskies.kubernetes_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;

@ControllerAdvice // Enables global handling
public class GlobalExceptionHandler {

    Logger logger= LoggerFactory.getLogger(getClass());
    @ExceptionHandler(ResourceNotFoundException.class) // Handles a specific custom exception
    public ResponseEntity<Map<String,String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Return a custom error response with a specific HTTP status
        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .body(Map.of("error",ex.getMessage()));
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(NoResourceFoundException ex) {
        // Return a custom error response with a specific HTTP status
        return "notfound";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        var errors = ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();

        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(Map.of("error",errors));
    }

    @ExceptionHandler(IllegalArgumentException.class) // Handles another specific exception
    public ResponseEntity<Map<String,String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(Map.of("error",ex.getMessage()));
    }



    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String,Object>> handleNotFoundException(HttpRequestMethodNotSupportedException ex) {

        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .body(Map.of("error",ex.getMessage()));
    }

    // Can also have a generic handler for other exceptions
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex) {
        logger.debug("Exception of type {}",ex.getClass().getName());
        return  "error";
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND) // Optional annotation for clarity
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
