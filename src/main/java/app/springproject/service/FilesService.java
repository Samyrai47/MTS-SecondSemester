package app.springproject.service;

import app.springproject.exception.FileAlreadyExistsException;
import app.springproject.exception.FileNotFoundException;
import app.springproject.repository.FilesRepositoryImpl;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesService {
  private final FilesRepositoryImpl filesRepository;
  // Потокобезопасен
  private final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

  // ExactlyOnce
  @Cacheable(
      cacheNames = {"createdFile"},
      key = "{#name}")
  public void createFile(String name, String value) throws FileAlreadyExistsException {
    log.info("Service -> adding new file {}", name);
    if (!processedFiles.add(name)) {
      log.info("File {} was already processed", name);
      return;
    }
    filesRepository.create(name, value);
  }

  @Cacheable(
      cacheNames = {"removedFile"},
      key = "{#name}")
  public void removeFile(String name) {
    log.info("Service -> removing file {}", name);
    filesRepository.delete(name);
  }

  @Cacheable(
      cacheNames = {"renamedFile"},
      key = "{#newName}")
  public void renameFile(String oldName, String newName)
      throws FileAlreadyExistsException, FileNotFoundException {
    log.info("Service -> Renaming file {} to {}", oldName, newName);
    filesRepository.rename(oldName, newName);
  }

  @Cacheable(
      cacheNames = {"changedContent"},
      key = "{#newValue}")
  public void changeContent(String name, String newValue) throws FileNotFoundException {
    log.info("Service -> Changing content of the file {}", name);
    filesRepository.changeContent(name, newValue);
  }
}
