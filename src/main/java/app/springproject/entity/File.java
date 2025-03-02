package app.springproject.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "File", description = "Сущность файла")
public record File(
    @Schema(description = "Имя файла", example = "FileName", type = "string") String name,
    @Schema(description = "Содержимое файла", example = "File's content.", type = "string")
        String content) {}
