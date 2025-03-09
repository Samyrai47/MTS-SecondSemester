package app.springproject.controller;

import app.springproject.dto.UserDto;
import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import app.springproject.service.UsersService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
@RateLimiter(name = "rateLimiterAPI")
public class UsersControllerImpl implements UsersController {
  private final UsersService usersService;

  @Override
  @PostMapping("/signin")
  public ResponseEntity<String> authenticate(@RequestBody User user)
      throws UserNotFoundException, AuthenticationDataMismatchException {
    usersService.authenticate(user.getEmail(), user.getPassword());
    log.info("Successfully logged in with name {}", user.getEmail());
    return ResponseEntity.ok("You have successfully logged in!");
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<UserDto> registerUser(@RequestBody User user)
      throws UserAlreadyExistsException, UserNotFoundException {
    usersService.registerUser(user);
    return ResponseEntity.status(201)
        .body(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
  }

  @Override
  @PatchMapping("/update")
  public ResponseEntity<UserDto> updateUser(@RequestBody User user) throws UserNotFoundException {
    usersService.updateUser(user);
    return ResponseEntity.ok(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
  }

  @Override
  @DeleteMapping("/delete/{email}")
  public ResponseEntity<UserDto> deleteUser(@PathVariable String email)
      throws UserNotFoundException {
    User user = usersService.deleteUser(email);
    return ResponseEntity.ok(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
  }

  @Override
  @GetMapping("/main")
  public ResponseEntity<List<UserDto>> getAll() {
    return ResponseEntity.ok(usersService.getAll());
  }

  @Override
  @GetMapping("/{username}")
  public ResponseEntity<UserDto> getByUsername(@PathVariable String username)
      throws UserNotFoundException {
    User user = usersService.getByUsername(username);
    return ResponseEntity.ok(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
  }
}
