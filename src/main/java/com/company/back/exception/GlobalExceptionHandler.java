package com.company.back.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<String> handleInvalidCredentials(
      InvalidCredentialsException ex
  ) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ex.getMessage());
  }
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntime(RuntimeException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(
      MethodArgumentNotValidException ex
  ) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
      .getFieldErrors()
      .forEach(error ->
          errors.put(error.getField(), error.getDefaultMessage())
      );

    return ResponseEntity.badRequest().body(errors);
  }
}
