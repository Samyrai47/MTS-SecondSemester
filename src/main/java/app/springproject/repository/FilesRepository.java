package app.springproject.repository;

import app.springproject.exception.FileAlreadyExistsException;
import app.springproject.exception.FileNotFoundException;

public interface FilesRepository {
  void create(String name, String value) throws FileAlreadyExistsException;

  void delete(String name);

  void rename(String oldName, String newName)
      throws FileNotFoundException, FileAlreadyExistsException;

  void changeContent(String name, String value) throws FileNotFoundException;
}
