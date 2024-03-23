package com.mpedroni.aprendatech.infra.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> result = ex.getAllErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.groupingBy(err -> "errors"));

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}