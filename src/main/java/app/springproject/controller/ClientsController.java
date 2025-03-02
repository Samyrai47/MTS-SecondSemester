package app.springproject.controller;

import app.springproject.exception.NumberGeneratorException;
import app.springproject.service.UsersService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/client")
@CircuitBreaker(name = "apiCircuitBreaker")
public class ClientsController {
  private final UsersService usersService;
  private Random random;

  @GetMapping("/random-url")
  public ResponseEntity<String> accessData() {
    return ResponseEntity.ok("RandomData");
  }

  @GetMapping("/random-number")
  public ResponseEntity<Integer> getNumber() throws NumberGeneratorException {
    if (Math.random() > 0.5) {
      throw new NumberGeneratorException("Failure");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(random.nextInt(100));
  }
}
