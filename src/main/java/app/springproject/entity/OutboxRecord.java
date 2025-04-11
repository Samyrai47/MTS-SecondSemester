package app.springproject.entity;

import app.springproject.dto.MessageDto;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull @Getter @Embedded private MessageDto value;

  public OutboxRecord(MessageDto value) {
    if (value == null) {
      throw new IllegalArgumentException("Value shouldn't be null.");
    }
    this.value = value;
  }
}
