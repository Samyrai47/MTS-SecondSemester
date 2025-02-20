package app.springproject.controller;

import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User API", description = "Управление пользователями")
public interface UsersController {
  @Operation(summary = "Аутентифицировать пользователя по имени и паролю")
  @ApiResponse(
      responseCode = "200",
      description = "Пользователь аутентифицирован",
      content = @Content)
  @ApiResponse(
      responseCode = "401",
      description = "UNAUTHORIZED | Неверные данные для пользователя",
      content = @Content)
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  public ResponseEntity<String> authenticate(@RequestBody User user)
      throws UserNotFoundException, AuthenticationDataMismatchException;

  @Operation(summary = "Зарегистрировать пользователя по имени и паролю")
  @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован")
  @ApiResponse(
      responseCode = "400",
      description = "BAD_REQUEST | Пользователь уже зарегистрирован",
      content = @Content)
  public ResponseEntity<User> registerUser(@RequestBody User user)
      throws UserAlreadyExistsException;

  @Operation(summary = "Изменить данные пользователя")
  @ApiResponse(responseCode = "200", description = "Данные о пользователе изменены")
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  public ResponseEntity<User> updateUser(@RequestBody User user) throws UserNotFoundException;

  @Operation(summary = "Удалить пользователя")
  @ApiResponse(responseCode = "200", description = "Пользователь удален")
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  public ResponseEntity<User> deleteUser(@PathVariable String username)
      throws UserNotFoundException;

  @Operation(summary = "Найти пользователя по имени")
  @ApiResponse(responseCode = "200", description = "Пользователь найден")
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Пользователь с такими данными не найден",
      content = @Content)
  public ResponseEntity<User> getByUsername(
      @Parameter(description = "Имя пользователя") String username) throws UserNotFoundException;

  @Operation(summary = "Вывести всех пользователей")
  @ApiResponse(responseCode = "200", description = "Пользователи выведены")
  public ResponseEntity<List<String>> getAll();
}
