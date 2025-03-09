package app.springproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  @NotNull(message = "Name should be filled")
  @Getter
  @Setter
  private String name;

  @Getter @Setter private String content;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @Getter
  private User user;

  public File(String name, String content, User user) {
    this.name = name;
    this.content = content;
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof File file)) return false;
    return Objects.equals(name, file.name)
        && Objects.equals(content, file.content)
        && Objects.equals(user, file.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, content, user);
  }
}
