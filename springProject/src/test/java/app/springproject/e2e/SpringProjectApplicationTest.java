package app.springproject.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import app.springproject.entity.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringProjectApplicationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void EndToEndTest() {
    /** Step 1: Create user */
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

    /** Step 2: Complete authentication */
    ResponseEntity<String> authenticationResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/second-memory/signin", mockUser, String.class);

    assertEquals(HttpStatus.OK, authenticationResponse.getStatusCode());

    /** Step 3: Update user */
    User updatedMockUser = new User("Boris12", "NewPassword");
    User updateResponse =
        restTemplate.patchForObject(
            "http://localhost:" + port + "/second-memory/update", updatedMockUser, User.class);

    assertEquals(updatedMockUser, updateResponse);

    /** Step 4: Get user */
    ResponseEntity<User> getUserResponse =
        restTemplate.getForEntity(
            "http://localhost:" + port + "/second-memory/Boris12", User.class);

    assertEquals(HttpStatus.OK, getUserResponse.getStatusCode());
    assertEquals(updatedMockUser, getUserResponse.getBody());

    /** Step 5: Get all users */
    List<String> userArrayResponse =
        restTemplate.getForObject("http://localhost:" + port + "/second-memory/main", List.class);

    assertEquals(2, userArrayResponse.size());
    assertEquals(updatedMockUser.username(), userArrayResponse.get(1));

    /** Step 6: Delete user */
    ResponseEntity<User> deleteResponse =
        restTemplate.exchange(
            "http://localhost:" + port + "/second-memory/delete/Boris12",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            User.class);

    assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    assertEquals(updatedMockUser, deleteResponse.getBody());
  }
}
