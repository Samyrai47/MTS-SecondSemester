package app.springproject.controller;

import app.springproject.entity.File;
import app.springproject.exception.FileAlreadyExistsException;
import app.springproject.exception.FileNotFoundException;
import app.springproject.service.FilesService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/second-memory/files")
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "apiCircuitBreaker")
public class FilesControllerImpl implements FilesController {
  private final FilesService filesService;

  @Override
  @PostMapping("/{fileName}")
  public ResponseEntity<File> createFile(@PathVariable String fileName, @RequestBody String content)
      throws FileAlreadyExistsException {
    log.info("Controller -> received create request for {}", fileName);
    filesService.createFile(fileName, content);
    log.info("Controller -> added file {}", fileName);
    return ResponseEntity.status(HttpStatus.CREATED).body(new File(fileName, content));
  }

  @Override
  @DeleteMapping("/{fileName}")
  public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
    log.info("Controller -> received delete request for {}", fileName);
    filesService.removeFile(fileName);
    log.info("Controller -> deleted file {}", fileName);
    return ResponseEntity.ok(String.format("File %s was deleted", fileName));
  }

  @Override
  @PatchMapping("/{oldName}")
  public ResponseEntity<String> renameFile(
      @PathVariable String oldName, @RequestBody String newName)
      throws FileAlreadyExistsException, FileNotFoundException {
    log.info("Controller -> received rename request from {} to {}", oldName, newName);
    filesService.renameFile(oldName, newName);
    log.info("Controller -> renamed file {}", newName);
    return ResponseEntity.ok(String.format("File %s was renamed to %s", oldName, newName));
  }

  @Override
  @PatchMapping("/{fileName}/change")
  public ResponseEntity<String> changeContent(
      @PathVariable String fileName, @RequestBody String content) throws FileNotFoundException {
    log.info("Controller -> received content change request {}", fileName);
    filesService.changeContent(fileName, content);
    log.info("Controller -> changed content for {}", fileName);
    return ResponseEntity.ok(String.format("Content of file %s was changed", fileName));
  }
}
