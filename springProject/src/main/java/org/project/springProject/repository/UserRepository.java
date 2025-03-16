package org.project.springProject.repository;

import org.project.springProject.entity.User;
import org.project.springProject.exception.AuthenticationDataMismatchException;
import org.project.springProject.exception.UserAlreadyExistsException;
import org.project.springProject.exception.UserNotFoundException;

public interface UserRepository {

  void authenticate(String name, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException;

  void registerUser(User user) throws UserAlreadyExistsException;
}
