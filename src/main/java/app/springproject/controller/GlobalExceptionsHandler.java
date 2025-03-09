package app.springproject.controller;

import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.FileAlreadyExistsException;
import app.springproject.exception.FileNotFoundException;
import app.springproject.exception.NumberGeneratorException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionsHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(AuthenticationDataMismatchException.class)
  public ResponseEntity<String> handleDataMismatch(AuthenticationDataMismatchException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(RequestNotPermitted.class)
  public ResponseEntity<String> handleFallback(RequestNotPermitted exception) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(exception.getMessage());
  }

  @ExceptionHandler(CallNotPermittedException.class)
  public ResponseEntity<String> handleGeneratorException(CallNotPermittedException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(NumberGeneratorException.class)
  public ResponseEntity<String> handleGeneratorException(NumberGeneratorException exception) {
    return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(exception.getMessage());
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<String> handleFileNotFound(FileNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(FileAlreadyExistsException.class)
  public ResponseEntity<String> handleFileAlreadyExists(FileAlreadyExistsException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }
}
