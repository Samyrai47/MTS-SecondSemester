package app.springproject.repository;

import static jakarta.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.junit.jupiter.api.Assertions.*;

import app.springproject.entity.User;
import app.springproject.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Transactional(value = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
class UsersRepositoryTest {

  @Container @ServiceConnection
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16");

  @Autowired UsersRepository usersRepository;

  @Test
  void findByEmailEquals() throws UserNotFoundException {
    User testUser = usersRepository.save(new User("boris@gmail.com", "Boris", "Boris123"));
    User responseUser = usersRepository.findByEmailEquals("boris@gmail.com").orElseThrow();

    assertEquals(testUser, responseUser);
  }

  @Test
  void findByNameLike() throws UserNotFoundException {
    User testUser = usersRepository.save(new User("boris@gmail.com", "Boris", "Boris123"));
    User responseUser = usersRepository.findByNameLike("Boris").orElseThrow();

    assertEquals(testUser, responseUser);
  }

  @Test
  void findAll() {
    User testUser1 = usersRepository.save(new User("boris@gmail.com", "Boris", "Boris123"));
    User testUser2 = usersRepository.save(new User("boris@gmail.com", "Boris", "Boris123"));
    List<User> testUserList = new ArrayList<>();
    testUserList.add(testUser1);
    testUserList.add(testUser2);

    List<User> responseUserList = usersRepository.findAll();

    assertEquals(testUserList, responseUserList);
  }

  @Test
  void findById() {
    User testUser = usersRepository.save(new User("boris@gmail.com", "Boris", "Boris123"));
    System.out.println(testUser.getId());
    User responseUser = usersRepository.findById(4L).orElseThrow();

    assertEquals(testUser, responseUser);
  }
}
