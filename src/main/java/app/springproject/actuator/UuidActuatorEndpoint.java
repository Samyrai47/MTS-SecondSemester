package app.springproject.actuator;

import java.util.UUID;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "uuid")
public class UuidActuatorEndpoint {

  @ReadOperation
  public UUID uuidInfo() {
    return UUID.randomUUID();
  }
}
