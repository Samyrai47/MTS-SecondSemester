package org.springproject.repository;

import java.util.List;
import org.springproject.entity.User;
import org.springproject.exception.AuthenticationDataMismatchException;
import org.springproject.exception.UserAlreadyExistsException;
import org.springproject.exception.UserNotFoundException;

public interface UserRepository {

  void authenticate(String name, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException;

  void registerUser(User user) throws UserAlreadyExistsException;

  void updateUser(User user) throws UserNotFoundException;

  User deleteUser(String username) throws UserNotFoundException;

  User getByUsername(String username) throws UserNotFoundException;

  List<String> getAll();
}
