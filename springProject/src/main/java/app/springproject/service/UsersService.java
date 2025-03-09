package app.springproject.service;

import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import app.springproject.repository.UsersRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
  private final UsersRepository userRepository;

  public void authenticate(String name, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException {
    userRepository.authenticate(name, password);
    log.info("Successful authentication for {}", name);
  }

  public void registerUser(String name, String password) throws UserAlreadyExistsException {
    log.info("Some service logic about registration");
    User user = new User(name, password);
    userRepository.registerUser(user);
  }

  public void updateUser(User user) throws UserNotFoundException {
    log.info("Some service logic about updating");
    userRepository.updateUser(user);
  }

  public User deleteUser(String username) throws UserNotFoundException {
    log.info("Some service logic about deleting");
    return userRepository.deleteUser(username);
  }

  public User getByUsername(String username) throws UserNotFoundException {
    log.info("Some service logic");
    return userRepository.getByUsername(username);
  }

  public List<String> getAll() {
    log.info("Some service logic about creating some page and searching for users");
    return userRepository.getAll();
  }
}
