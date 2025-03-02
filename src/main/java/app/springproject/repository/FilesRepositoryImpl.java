package app.springproject.repository;

import app.springproject.exception.FileAlreadyExistsException;
import app.springproject.exception.FileNotFoundException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class FilesRepositoryImpl implements FilesRepository {
  private HashMap<String, String> files = new HashMap<>();

  @Override
  public void create(String name, String value) throws FileAlreadyExistsException {
    if (files.containsKey(name)) {
      throw new FileAlreadyExistsException(String.format("File %s already exists", name));
    } else {
      files.put(name, value);
      log.info("Added new file: {}", name);
    }
  }

  @Override
  public void delete(String name) {
    if (!files.containsKey(name)) {
      files.remove(name);
    }
  }

  @Override
  public void rename(String oldName, String newName)
      throws FileNotFoundException, FileAlreadyExistsException {
    if (!files.containsKey(oldName)) {
      throw new FileNotFoundException(String.format("File %s doesn't exists", oldName));
    } else if (files.containsKey(newName)) {
      throw new FileAlreadyExistsException(
          String.format("File %s already exists, please, choose another name for file", newName));
    } else {
      String tempValue = files.get(oldName);
      files.remove(oldName);
      files.put(newName, tempValue);
      log.info("File's name was changed: {} -> {}", oldName, newName);
    }
  }

  @Override
  public void changeContent(String name, String value) throws FileNotFoundException {
    if (!files.containsKey(name)) {
      throw new FileNotFoundException(String.format("File %s doesn't exists", name));
    } else {
      files.replace(name, value);
      log.info("File's value was changed: {}", name);
    }
  }
}
