package org.project.springProject.repository;

import java.util.HashMap;
import org.project.springProject.entity.User;
import org.project.springProject.exception.AuthenticationDataMismatchException;
import org.project.springProject.exception.UserAlreadyExistsException;
import org.project.springProject.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
  Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
  private static HashMap<String, String> users = new HashMap<>();

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
    logger.info("{} registered", user.username());
  }
}
