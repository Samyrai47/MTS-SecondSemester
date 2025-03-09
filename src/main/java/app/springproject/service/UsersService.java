package app.springproject.service;

import app.springproject.dto.UserDto;
import app.springproject.entity.File;
import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.FileAlreadyExistsException;
import app.springproject.exception.FileNotFoundException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import app.springproject.repository.FilesRepository;
import app.springproject.repository.UsersRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UsersService {
  private final UsersRepository userRepository;
  private final FilesRepository filesRepository;

  public void authenticate(String email, String password)
      throws AuthenticationDataMismatchException, UserNotFoundException {
    User user = userRepository.findByEmailEquals(email).orElseThrow(UserNotFoundException::new);
    if (!user.getPassword().equals(password)) {
      throw new AuthenticationDataMismatchException("Wrong password for user " + user.getEmail());
    }
    log.info("Successful authentication for {}", email);
  }

  public void registerUser(User newUser) throws UserAlreadyExistsException, UserNotFoundException {
    log.info("Some service logic about registration");
    userRepository
        .findByEmailEquals(newUser.getEmail())
        .ifPresent(
            user -> {
              throw new UserAlreadyExistsException(
                  "User with email " + user.getEmail() + " already exists.");
            });
    userRepository.save(newUser);
  }

  @Async
  public void updateUser(User updatedUser) throws UserNotFoundException {
    User user =
        userRepository
            .findByEmailEquals(updatedUser.getEmail())
            .orElseThrow(UserNotFoundException::new);
    log.info("Some service logic about updating");
    user.setName(updatedUser.getName());
    user.setPassword(updatedUser.getPassword());
    userRepository.save(user);
  }

  public User deleteUser(String email) throws UserNotFoundException {
    User user = userRepository.findByEmailEquals(email).orElseThrow(UserNotFoundException::new);
    log.info("Some service logic about deleting");
    userRepository.delete(user);
    return user;
  }

  public User getByUsername(String username) throws UserNotFoundException {
    log.info("Some service logic");
    return userRepository.findByNameLike(username).orElseThrow(UserNotFoundException::new);
  }

  public List<UserDto> getAll() {
    log.info("Some service logic about creating some page and searching for users");
    List<User> userList = userRepository.findAll();
    List<UserDto> userDtos = new ArrayList<>();
    for (User user : userList) {
      userDtos.add(new UserDto(user.getEmail(), user.getName(), user.getFiles()));
    }
    return userDtos;
  }

  public void deleteFile(Long fileId) throws FileNotFoundException, UserNotFoundException {
    File file = filesRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    User user =
        userRepository.findById(file.getUser().getId()).orElseThrow(UserNotFoundException::new);
    filesRepository.delete(file);
    user.getFiles().remove(file);
    userRepository.save(user);
  }

  public File createFile(Long userId, String name, String content) throws UserNotFoundException {
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    File file = new File(name, content, user);
    List<File> fileList = user.getFiles();
    if (fileList.contains(file)) {
      throw new FileAlreadyExistsException("File with name " + name + "already exists");
    }
    fileList.add(file);
    userRepository.save(user);
    return file;
  }
}
