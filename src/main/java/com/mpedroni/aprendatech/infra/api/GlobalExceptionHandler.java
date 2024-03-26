package com.mpedroni.aprendatech.infra.api;

import com.mpedroni.aprendatech.domain.courses.exceptions.CourseCodeAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.exceptions.EmailAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.exceptions.UserNotFoundException;
import com.mpedroni.aprendatech.domain.users.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {UsernameAlreadyExistsException.class, EmailAlreadyExistsException.class})
    public ResponseEntity<?> handleConflict(RuntimeException ex, WebRequest request) {
        var body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        body.put("exception", ex.getClass().getSimpleName());
        body.put("message", ex.getMessage());

        return handleExceptionInternal(
            ex,
            body,
            new HttpHeaders(),
            HttpStatus.CONFLICT,
            request
        );
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        var status = HttpStatus.UNAUTHORIZED;

        var body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("exception", ex.getClass().getSimpleName());
        body.put("message", "Username or password is incorrect.");

        return handleExceptionInternal(
            ex,
            body,
            new HttpHeaders(),
            status,
            request
        );
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;

        var body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("exception", ex.getClass().getSimpleName());
        body.put("message", ex.getMessage());

        return handleExceptionInternal(
            ex,
            body,
            new HttpHeaders(),
            status,
            request
        );
    }

    @ExceptionHandler(value = {CourseCodeAlreadyExistsException.class})
    public ResponseEntity<?> handleCourseCodeAlreadyExists(CourseCodeAlreadyExistsException ex, WebRequest request) {
        var status = HttpStatus.CONFLICT;

        var body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("exception", ex.getClass().getSimpleName());
        body.put("message", ex.getMessage());

        return handleExceptionInternal(
            ex,
            body,
            new HttpHeaders(),
            status,
            request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        var body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", status.value());
        body.put("error", status.value());
        body.put("exception", ex.getClass().getSimpleName());
        body.put("errors", errors);

        return handleExceptionInternal(
                ex,
                body,
                headers,
                status,
                request
        );
    }

}
