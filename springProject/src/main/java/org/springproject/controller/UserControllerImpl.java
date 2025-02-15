package org.springproject.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springproject.entity.User;
import org.springproject.exception.AuthenticationDataMismatchException;
import org.springproject.exception.UserAlreadyExistsException;
import org.springproject.exception.UserNotFoundException;
import org.springproject.service.UserService;

@RestController
@Slf4j
public class UserControllerImpl implements UserController {
  private final UserService userService;

  public UserControllerImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  @PostMapping("/signin")
  public ResponseEntity<String> authenticate(@RequestBody User user)
      throws UserNotFoundException, AuthenticationDataMismatchException {
    userService.authenticate(user.username(), user.password());
    log.info("Successfully logged in with name {}", user.username());
    return ResponseEntity.ok("You have successfully logged in!");
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<User> registerUser(@RequestBody User user)
      throws UserAlreadyExistsException {
    userService.registerUser(user.username(), user.password());
    return ResponseEntity.status(201).body(user);
  }

  @Override
  @PatchMapping("/update")
  public ResponseEntity<User> updateUser(@RequestBody User user) throws UserNotFoundException {
    userService.updateUser(user);
    return ResponseEntity.ok(user);
  }

  @Override
  @DeleteMapping("/delete/{username}")
  public ResponseEntity<User> deleteUser(@PathVariable String username)
      throws UserNotFoundException {
    User user = userService.deleteUser(username);
    return ResponseEntity.ok(user);
  }

  @Override
  @GetMapping(value = "/main")
  public ResponseEntity<List<String>> getAll() {
    return ResponseEntity.ok(userService.getAll());
  }

  @Override
  @GetMapping("/{username}")
  public ResponseEntity<User> getByUsername(@PathVariable String username)
      throws UserNotFoundException {
    User user = userService.getByUsername(username);
    return ResponseEntity.ok(user);
  }
}
