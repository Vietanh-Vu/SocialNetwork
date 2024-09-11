package com.example.socialnetwork.exception.handler;

import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.ConflictException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(Exception e, HttpServletRequest request, HttpStatus status) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(Instant.now())
                .build();
    }

    private ErrorResponse buildErrorResponse(String message, HttpServletRequest request, HttpStatus status) {
        return ErrorResponse.builder()
                .message(message)
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }

        String errorString = String.join(", ", errors);

        return buildErrorResponse(errorString, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errors);
        return buildErrorResponse(errorMessage, request, HttpStatus.BAD_REQUEST);
    }

    private String resolveFieldName(String fieldName, Object targetObject) {
        try {
            if (targetObject == null) {
                return fieldName;
            }
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
            if (jsonPropertyAnnotation != null) {
                return jsonPropertyAnnotation.value();
            }
        } catch (NoSuchFieldException e) {
            return fieldName;
        }
        return fieldName;
    }

    @ExceptionHandler({
            InvalidMediaTypeException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            ClientErrorException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class,
            NotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            InsufficientAuthenticationException.class,
            AccessDeniedException.class,
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            AuthenticationException.class,
            InvalidBearerTokenException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, request, HttpStatus.UNAUTHORIZED);
    }
}
