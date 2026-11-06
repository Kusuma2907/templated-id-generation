package com.example.id_generator.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnknownTemplateException.class)
    public ResponseEntity<ApiError> handleUnknownTemplate(
            UnknownTemplateException exception,
            HttpServletRequest request) {

        return error(
                HttpStatus.NOT_FOUND,
                "UNKNOWN_TEMPLATE",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(UnknownCityException.class)
    public ResponseEntity<ApiError> handleUnknownCity(
            UnknownCityException exception,
            HttpServletRequest request) {

        return error(
                HttpStatus.NOT_FOUND,
                "UNKNOWN_CITY",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(
            MissingCounterConfigurationException.class
    )
    public ResponseEntity<ApiError>
    handleMissingCounter(
            MissingCounterConfigurationException exception,
            HttpServletRequest request) {

        return error(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "MISSING_COUNTER_CONFIGURATION",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(InvalidTemplateException.class)
    public ResponseEntity<ApiError> handleInvalidTemplate(
            InvalidTemplateException exception,
            HttpServletRequest request) {

        return error(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "INVALID_TEMPLATE",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String message =
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(error ->
                                error.getField()
                                        + ": "
                                        + error.getDefaultMessage())
                        .orElse("Request validation failed");

        return error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                message,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {

        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                request
        );
    }

    private ResponseEntity<ApiError> error(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request) {

        ApiError body =
                new ApiError(
                        Instant.now(),
                        status.value(),
                        error,
                        message,
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(status)
                .body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<ApiError> handleIllegalArgument(
        IllegalArgumentException exception,
        HttpServletRequest request) {

    return error(
            HttpStatus.BAD_REQUEST,
            "BAD_REQUEST",
            exception.getMessage(),
            request
    );
}

}