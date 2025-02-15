package org.project.springProject.repository;

import java.util.List;
import org.project.springProject.entity.User;
import org.project.springProject.exception.AuthenticationDataMismatchException;
import org.project.springProject.exception.UserAlreadyExistsException;
import org.project.springProject.exception.UserNotFoundException;

public interface UserRepository {

  void authenticate(String name, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException;

  void registerUser(User user) throws UserAlreadyExistsException;

  void updateUser(User user) throws UserNotFoundException;

  User deleteUser(String username) throws UserNotFoundException;

  User getByUsername(String username) throws UserNotFoundException;

  List<String> getAll();
}
