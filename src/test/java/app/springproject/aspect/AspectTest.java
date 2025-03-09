package app.springproject.aspect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AspectTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private MethodCallLoggingAspect methodCallLoggingAspect;

  @Test
  void shouldIncreaseClassFieldByTwo() {
    int fieldValueBeforeExecution = methodCallLoggingAspect.getExecutionCount();
    String randomUrlRequest =
        restTemplate.getForObject("http://localhost:" + port + "/client/random-url", String.class);
    System.out.println(randomUrlRequest);

    assertEquals(fieldValueBeforeExecution + 2, methodCallLoggingAspect.getExecutionCount());
  }
}
