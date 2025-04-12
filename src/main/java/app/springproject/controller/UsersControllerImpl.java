package app.springproject.controller;

import app.springproject.dto.UserDto;
import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import app.springproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
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
@RequestMapping("/second-memory")
@RateLimiter(name = "rateLimiterAPI")
@Timed(
    value = "request.duration",
    description = "HTTP requests duration",
    percentiles = {0.5, 0.95, 0.99},
    histogram = true)
public class UsersControllerImpl implements UsersController {
  private final UsersService usersService;
  private final Counter totalRequests;
  private final Counter registrationRequests;
  private final Counter authenticationRequests;
  private final Counter updateRequests;
  private final Counter deleteRequests;

  public UsersControllerImpl(UsersService usersService, MeterRegistry registry) {
    this.usersService = usersService;
    this.registrationRequests = Counter.builder("user.requests.registration").register(registry);
    this.authenticationRequests =
        Counter.builder("user.requests.authentication").register(registry);
    this.updateRequests = Counter.builder("user.requests.update").register(registry);
    this.deleteRequests = Counter.builder("user.requests.delete").register(registry);
    this.totalRequests = Counter.builder("user.requests").register(registry);
  }

  @Override
  @PostMapping("/signin")
  public ResponseEntity<String> authenticate(@RequestBody User user)
      throws UserNotFoundException, AuthenticationDataMismatchException, JsonProcessingException {
    authenticationRequests.increment();
    totalRequests.increment();
    usersService.authenticate(user.getEmail(), user.getPassword());
    log.info("Successfully logged in with name {}", user.getEmail());
    return ResponseEntity.ok()
        .header("userId", String.valueOf(user.getId()))
        .body("You have successfully logged in!");
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<UserDto> registerUser(@RequestBody User user)
      throws UserAlreadyExistsException, UserNotFoundException, JsonProcessingException {
    registrationRequests.increment();
    totalRequests.increment();
    usersService.registerUser(user);
    return ResponseEntity.status(201)
        .body(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
  }

  @Override
  @PatchMapping("/update")
  public ResponseEntity<UserDto> updateUser(@RequestBody User user)
      throws UserNotFoundException, JsonProcessingException {
    updateRequests.increment();
    totalRequests.increment();
    usersService.updateUser(user);
    return ResponseEntity.ok()
        .header("userId", String.valueOf(user.getId()))
        .body(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
  }

  @Override
  @DeleteMapping("/delete/{email}")
  public ResponseEntity<UserDto> deleteUser(@PathVariable String email)
      throws UserNotFoundException, JsonProcessingException {
    deleteRequests.increment();
    totalRequests.increment();
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
    return ResponseEntity.ok()
        .header("userId", String.valueOf(user.getId()))
        .body(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
  }
}
