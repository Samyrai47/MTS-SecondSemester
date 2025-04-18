package app.springproject.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class MessageDto {
  private Long userId;

  private Action action;

  private MessageDto() {}
}
