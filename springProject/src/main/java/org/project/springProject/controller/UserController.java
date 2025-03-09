package org.project.springProject.controller;

import org.project.springProject.entity.User;
import org.project.springProject.exception.AuthenticationDataMismatchException;
import org.project.springProject.exception.UserAlreadyExistsException;
import org.project.springProject.exception.UserNotFoundException;
import org.project.springProject.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
  private final UserService userService;
  Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
  }

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

  @GetMapping("/signin")
  public ResponseEntity<String> authenticate(@RequestBody User user)
      throws UserNotFoundException, AuthenticationDataMismatchException {
    userService.authenticate(user.username(), user.password());
    logger.info("Successfully logged in with name {}", user.username());
    return ResponseEntity.ok("You have successfully logged in!");
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@RequestBody User user)
      throws UserAlreadyExistsException {
    userService.registerUser(user.username(), user.password());
    return new ResponseEntity<>("You have successfully registered!", HttpStatus.CREATED);
  }
}
