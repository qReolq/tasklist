package qreol.project.tasklist.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import qreol.project.tasklist.domain.exception.*;
import qreol.project.tasklist.web.dto.exception.ExceptionBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFound(ResourceNotFoundException e) {
        return new ExceptionBody(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ResourceMappingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleResourceMapping(ResourceMappingException e) {
        return new ExceptionBody(e.getMessage(), LocalDateTime.now());
    }

    //Bad credentials
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleResourceIllegalState(IllegalStateException e) {
        return new ExceptionBody(e.getMessage(), LocalDateTime.now());
    }

    //Access denied
    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleAccessDenied() {
        return new ExceptionBody("Access denied", LocalDateTime.now());
    }

    //Invalid user (dto)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed", LocalDateTime.now());
        exceptionBody.setErrors(mapListToMap(e.getBindingResult().getFieldErrors()));

        return exceptionBody;
    }

    // Custom validation
    @ExceptionHandler(ResourceNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolation(ResourceNotValidException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed", LocalDateTime.now());
        exceptionBody.setErrors(mapListToMap(e.getErrors()));

        return exceptionBody;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleAuthentication(AuthenticationException e) {
        return new ExceptionBody("Authentication failed", LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(Exception e) {
        return new ExceptionBody("Internal error", LocalDateTime.now());
    }

    private Map<String, String> mapListToMap(List<FieldError> errors) {
        return errors
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @ExceptionHandler(ImageUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleImageUpload(ImageUploadException e) {
        return new ExceptionBody(e.getMessage(), LocalDateTime.now());
    }

}
