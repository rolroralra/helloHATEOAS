package com.example.oas.controller;

import com.example.oas.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

  @SuppressWarnings("unused")
  @ExceptionHandler
  public ResponseEntity<?> notFoundException(NotFoundException exception) {
    return ResponseEntity.notFound().build();
  }
}
