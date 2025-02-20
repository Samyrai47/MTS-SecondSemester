package app.springproject.repository;

import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UsersRepositoryImpl implements UsersRepository {
  private HashMap<String, String> users = new HashMap<>();
  private final String NOT_FOUND_MESSAGE = "User %s was not found";

  @Override
  public void authenticate(String name, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException {
    if (users.containsKey(name)) {
      if (!users.get(name).equals(password)) {
        throw new AuthenticationDataMismatchException(
            String.format("Wrong data for authentication: %s", name));
      }
    } else {
      throw new UserNotFoundException(String.format("No such user: %s", name));
    }
  }

  @Override
  public void registerUser(User user) throws UserAlreadyExistsException {
    if (users.containsKey(user.username())) {
      throw new UserAlreadyExistsException(
          String.format("User %s is already exists", user.username()));
    }
    users.put(user.username(), user.password());
    log.info("{} registered", user.username());
  }

  @Override
  public void updateUser(User user) throws UserNotFoundException {
    if (users.containsKey(user.username())) {
      users.replace(user.username(), user.password());
      log.info("User {} was updated", user.username());
    } else {
      throw new UserNotFoundException(String.format(NOT_FOUND_MESSAGE, user.username()));
    }
  }

  @Override
  public User deleteUser(String username) throws UserNotFoundException {
    if (users.containsKey(username)) {
      User user = new User(username, users.get(username));
      users.remove(username);
      log.info("User {} was deleted", username);
      return user;
    } else {
      throw new UserNotFoundException(String.format(NOT_FOUND_MESSAGE, username));
    }
  }

  @Override
  public User getByUsername(String username) throws UserNotFoundException {
    if (users.containsKey(username)) {
      User user = new User(username, users.get(username));
      log.info("Getting user {}", username);
      return user;
    } else {
      throw new UserNotFoundException(String.format(NOT_FOUND_MESSAGE, username));
    }
  }

  @Override
  public List<String> getAll() {
    return new ArrayList<String>(users.keySet());
  }
}
