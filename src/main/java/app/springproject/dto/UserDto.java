package app.springproject.dto;

import app.springproject.entity.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class UserDto {
  @Getter private String email;
  @Getter private String name;
  @Getter private List<FileDto> files;

  public UserDto(String email, String name, List<File> files) {
    this.email = email;
    this.name = name;
    this.files = new ArrayList<>();
    for (File file : files) {
      this.files.add(new FileDto(file));
    }
  }
}
