package app.springproject.repository;

import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import java.util.List;

public interface UsersRepository {

  void authenticate(String name, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException;

  void registerUser(User user) throws UserAlreadyExistsException;

  void updateUser(User user) throws UserNotFoundException;

  User deleteUser(String username) throws UserNotFoundException;

  User getByUsername(String username) throws UserNotFoundException;

  List<String> getAll();
}
