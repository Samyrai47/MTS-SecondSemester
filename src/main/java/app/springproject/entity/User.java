package app.springproject.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  @NotNull(message = "Email should be filled")
  @Getter
  private String email;

  @NotNull(message = "Name should be filled")
  @Getter
  @Setter
  private String name;

  @NotNull(message = "Password should be filled")
  @Getter
  @Setter
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @Getter
  private List<File> files = new ArrayList<>();

  public User(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }
}
