package com.example.common.application.middleware;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.common.domain.util.AppUtil;
import com.example.security.exception.AppException;
import com.example.security.exception.ErrorMessages;
import com.example.security.exception.detail.ForbiddenException;
import com.example.security.exception.detail.InvalidJsonFormatException;
import com.example.security.exception.detail.RequestMethodNotAllowedException;
import com.example.security.exception.detail.SomethingWentWrongException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Global exception handler
 */
@Log4j2
@RestControllerAdvice
public class AppExceptionHandler {
    @Value(AppUtil.ENV_PATH_DEBUG)
    private boolean isDebug;

    /**
     * Handle app exceptions
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorMessages> handleAppException(AppException exception) {
        log.warn(AppException.class + " handler: " + exception.getMessage());

        return responseErrorMessages(List.of(exception.getMessage()), exception.getStatus());
    }

    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessages> handleValidationError(MethodArgumentNotValidException exception) {
        List<String> messages = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::createFieldErrorMessage).collect(Collectors.toList());

        log.warn(MethodArgumentNotValidException.class + " handler: " + exception.getMessage());

        return responseErrorMessages(messages, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle wrong JSON syntax error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessages> handleNotReadableException(HttpMessageNotReadableException exception) {
        log.warn(HttpMessageNotReadableException.class + " handler: " + exception.getMessage());

        /* Check if JSON parse error */
        if (
                Objects.requireNonNull(exception.getMessage()).contains("JSON parse error") ||
                        Objects.requireNonNull(exception.getMessage()).contains("Required request body is missing")
        ) {
            return handleAppException(new InvalidJsonFormatException());
        }

        return handleException(exception);
    }

    /**
     * Handle spring access denied exceptions
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessages> handleAccessDeniedException(AccessDeniedException exception) {
        log.warn(Exception.class + " handler: " + exception.getMessage());

        return handleAppException(new ForbiddenException());
    }

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessages> handleException(Exception exception) {
        String message = isDebug
                ? exception.getMessage()
                : (new SomethingWentWrongException()).getMessage();

        /* handle "Method not allowed" exception */
        if (
                Objects.requireNonNull(exception.getMessage()).contains("Request method") &&
                        Objects.requireNonNull(exception.getMessage()).contains("is not supported")
        ) {
            return handleAppException(new RequestMethodNotAllowedException());
        }

        log.warn(Exception.class + " handler: " + exception.getMessage());

        return responseErrorMessages(List.of(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Build error messages
     */
    private ResponseEntity<ErrorMessages> responseErrorMessages(List<String> messages, HttpStatus status) {
        ErrorMessages errorMessages = new ErrorMessages();
        messages.forEach(errorMessages::append);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorMessages, headers, status);
    }

    /**
     * Create validation error message
     */
    private String createFieldErrorMessage(FieldError fieldError) {
        return String.format(
                "[%s] %s. your input: [%s]",
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue()
        );
    }
}
