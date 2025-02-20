package app.springproject.controller;

import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import app.springproject.service.UsersService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class UsersControllerImpl implements UsersController {
  private final UsersService usersService;

  @Override
  @PostMapping("/signin")
  public ResponseEntity<String> authenticate(@RequestBody User user)
      throws UserNotFoundException, AuthenticationDataMismatchException {
    usersService.authenticate(user.username(), user.password());
    log.info("Successfully logged in with name {}", user.username());
    return ResponseEntity.ok("You have successfully logged in!");
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<User> registerUser(@RequestBody User user)
      throws UserAlreadyExistsException {
    usersService.registerUser(user.username(), user.password());
    return ResponseEntity.status(201).body(user);
  }

  @Override
  @PatchMapping("/update")
  public ResponseEntity<User> updateUser(@RequestBody User user) throws UserNotFoundException {
    usersService.updateUser(user);
    return ResponseEntity.ok(user);
  }

  @Override
  @DeleteMapping("/delete/{username}")
  public ResponseEntity<User> deleteUser(@PathVariable String username)
      throws UserNotFoundException {
    User user = usersService.deleteUser(username);
    return ResponseEntity.ok(user);
  }

  @Override
  @GetMapping(value = "/main")
  public ResponseEntity<List<String>> getAll() {
    return ResponseEntity.ok(usersService.getAll());
  }

  @Override
  @GetMapping("/{username}")
  public ResponseEntity<User> getByUsername(@PathVariable String username)
      throws UserNotFoundException {
    User user = usersService.getByUsername(username);
    return ResponseEntity.ok(user);
  }
}
