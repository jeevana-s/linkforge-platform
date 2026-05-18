package com.linkforge.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Map<String,Object>> api(ApiException e){
    return ResponseEntity.status(e.status).body(Map.of("error", e.getMessage(), "status", e.status.value()));
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e){
    var errors = new HashMap<String,String>();
    e.getBindingResult().getFieldErrors().forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));
    return ResponseEntity.badRequest().body(Map.of("error","Validation failed","fields",errors));
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,Object>> generic(Exception e){
    return ResponseEntity.status(500).body(Map.of("error", e.getMessage() == null ? "Internal error" : e.getMessage()));
  }
}
