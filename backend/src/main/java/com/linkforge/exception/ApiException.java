package com.linkforge.exception;
import org.springframework.http.HttpStatus;
public class ApiException extends RuntimeException {
  public final HttpStatus status;
  public ApiException(HttpStatus s, String msg){ super(msg); this.status=s; }
}
