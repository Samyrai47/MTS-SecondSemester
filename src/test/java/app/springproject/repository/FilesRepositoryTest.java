package app.springproject.repository;

import app.springproject.entity.File;
import app.springproject.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static jakarta.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional(value = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
class FilesRepositoryTest {

  @Container @ServiceConnection
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16");

  @Autowired FilesRepository filesRepository;
  @Autowired UsersRepository usersRepository;

  @Test
  void findByNameEquals() {
    User testUser = usersRepository.save(new User("boris1@gmail.com", "Boris", "Boris123"));
    File testFile =
        filesRepository.save(new File("javaOneLove", "Java is simply the best!", testUser));
    File responceFile = filesRepository.findByNameEquals("javaOneLove").orElseThrow();

    assertEquals(testFile, responceFile);
  }
}
