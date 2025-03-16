package org.project.springProject.service;

import org.project.springProject.entity.User;
import org.project.springProject.exception.AuthenticationDataMismatchException;
import org.project.springProject.exception.UserAlreadyExistsException;
import org.project.springProject.exception.UserNotFoundException;
import org.project.springProject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void authenticate(String name, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException {
    userRepository.authenticate(name, password);
    logger.info("Successful authentication for {}", name);
  }

  public void registerUser(String name, String password) throws UserAlreadyExistsException {
    logger.info("Some service logic");
    User user = new User(name, password);
    userRepository.registerUser(user);
  }
}
