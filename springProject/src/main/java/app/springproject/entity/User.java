package app.springproject.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User", description = "Сущность пользователя")
public record User(
    @Schema(description = "Имя пользователя", example = "Boris", type = "string") String username,
    @Schema(description = "Пароль", example = "Boris123", type = "string") String password) {}
