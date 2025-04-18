package app.springproject.service;

import app.springproject.entity.File;
import app.springproject.exception.FileNotFoundException;
import app.springproject.repository.FilesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FilesService {

  private final FilesRepository filesRepository;

  public void updateFileName(String oldName, String newName) throws FileNotFoundException {
    File file = filesRepository.findByNameEquals(oldName).orElseThrow(FileNotFoundException::new);
    log.info("Service -> Renaming file {} to {}", oldName, newName);
    file.setName(newName);
    filesRepository.save(file);
  }

  public void changeContent(String name, String newContent) throws FileNotFoundException {
    File file = filesRepository.findByNameEquals(name).orElseThrow(FileNotFoundException::new);
    log.info("Service -> Changing content of the file {}", name);
    file.setContent(newContent);
    filesRepository.save(file);
  }
}
