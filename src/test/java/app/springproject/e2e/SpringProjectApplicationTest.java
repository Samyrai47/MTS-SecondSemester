package app.springproject.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import app.springproject.entity.File;
import app.springproject.entity.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class SpringProjectApplicationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  static PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:13")
          .withInitScript("db-init-script.sql")
          .withDatabaseName("testdb")
          .withUsername("testuser")
          .withPassword("testpass");

  static {
    postgresContainer.start();
  }

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
  }

  @Test
  void EndToEndTest() {
    // Step 0: Get db info
    log.info("Db host: {}", postgresContainer.getHost());
    log.info("Db port: {}", postgresContainer.getFirstMappedPort());
    log.info("PostgreSQL is running at: {}", postgresContainer.getJdbcUrl());

    // Step 1: Create user
    User mockUser = new User("Boris12", "Boris123");
    User mockUser2 = new User("Test", "Test");
    ResponseEntity<User> registrationResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/second-memory/signup", mockUser, User.class);

    ResponseEntity<User> registrationResponse2 =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/second-memory/signup", mockUser2, User.class);

    assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
    assertEquals(mockUser, registrationResponse.getBody());
    assertEquals(HttpStatus.CREATED, registrationResponse2.getStatusCode());
    assertEquals(mockUser2, registrationResponse2.getBody());

    // Step 2: Update user
    User updatedMockUser = new User("Boris12", "NewPassword");
    User updateResponse =
        restTemplate.patchForObject(
            "http://localhost:" + port + "/second-memory/update", updatedMockUser, User.class);

    assertEquals(updatedMockUser, updateResponse);

    // Step 3: Get user
    ResponseEntity<User> getUserResponse =
        restTemplate.getForEntity(
            "http://localhost:" + port + "/second-memory/Boris12", User.class);

    assertEquals(HttpStatus.OK, getUserResponse.getStatusCode());
    assertEquals(updatedMockUser, getUserResponse.getBody());

    // Step 4: Get all users
    List<String> userArrayResponse =
        restTemplate.getForObject("http://localhost:" + port + "/second-memory/main", List.class);

    assertEquals(2, userArrayResponse.size());
    assertEquals(updatedMockUser.username(), userArrayResponse.get(1));

    // Step 5: Delete user
    ResponseEntity<User> deleteUserResponse =
        restTemplate.exchange(
            "http://localhost:" + port + "/second-memory/delete/Boris12",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            User.class);

    assertEquals(HttpStatus.OK, deleteUserResponse.getStatusCode());
    assertEquals(updatedMockUser, deleteUserResponse.getBody());

    // Step 6: Create file
    File mockFile = new File("test1", "test123");
    ResponseEntity<File> createFileResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/second-memory/files/javaOneLove",
            "Java is simply the best!",
            File.class);

    assertEquals(HttpStatus.CREATED, createFileResponse.getStatusCode());

    // Step 7: Rename file
    String renameFileRequest =
        restTemplate.patchForObject(
            "http://localhost:" + port + "/second-memory/files/javaOneLove",
            "assemblerOneLove",
            String.class);

    assertEquals("File javaOneLove was renamed to assemblerOneLove", renameFileRequest);

    // Step 8: Change file's content
    String changeContentResponse =
        restTemplate.patchForObject(
            "http://localhost:" + port + "/second-memory/files/assemblerOneLove/change",
            "Assembler is simply the best!",
            String.class);

    assertEquals("Content of file assemblerOneLove was changed", changeContentResponse);

    // Step 9: Delete file
    ResponseEntity<String> deleteFileResponse =
        restTemplate.exchange(
            "http://localhost:" + port + "/second-memory/files/assemblerOneLove",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            String.class);

    assertEquals(HttpStatus.OK, deleteFileResponse.getStatusCode());
  }
}
