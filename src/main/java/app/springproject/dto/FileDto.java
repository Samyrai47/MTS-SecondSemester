package app.springproject.dto;

import app.springproject.entity.File;
import lombok.Data;

@Data
public class FileDto {
  private String name;
  private String content;
  private Long userId;

  public FileDto(File file) {
    this.name = file.getName();
    this.content = file.getContent();
    this.userId = file.getUser().getId();
  }
}
