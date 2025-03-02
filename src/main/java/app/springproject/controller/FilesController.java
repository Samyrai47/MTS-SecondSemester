package app.springproject.controller;

import app.springproject.entity.File;
import app.springproject.exception.FileAlreadyExistsException;
import app.springproject.exception.FileNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Files API", description = "Управление файлами")
public interface FilesController {
  @Operation(summary = "Создание нового файла")
  @ApiResponse(responseCode = "201", description = "Файл создан", content = @Content)
  @ApiResponse(
      responseCode = "400",
      description = "BAD_REQUEST | Файл с таким названием уже существует",
      content = @Content)
  ResponseEntity<File> createFile(@PathVariable String fileName, @RequestBody String content)
      throws FileAlreadyExistsException;

  @Operation(summary = "Удаление файла")
  @ApiResponse(responseCode = "200", description = "Файл удален", content = @Content)
  ResponseEntity<String> deleteFile(@PathVariable String fileName);

  @Operation(summary = "Переименование файла")
  @ApiResponse(responseCode = "200", description = "Файл переименован", content = @Content)
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Файл с таким названием не найден",
      content = @Content)
  @ApiResponse(
      responseCode = "400",
      description =
          "BAD_REQUEST | Новое название для файла совпадает с уже существующим именем файла",
      content = @Content)
  ResponseEntity<String> renameFile(
      @Parameter(description = "Старое название файла") String oldName,
      @Parameter(description = "Новое название файла") String newName)
      throws FileAlreadyExistsException, FileNotFoundException;

  @Operation(summary = "Изменение содержимого файла")
  @ApiResponse(responseCode = "200", description = "Содержимое файла изменено", content = @Content)
  @ApiResponse(
      responseCode = "404",
      description = "NOT_FOUND | Файл с таким названием не найден",
      content = @Content)
  ResponseEntity<String> changeContent(
      @Parameter(description = "Название файла") String fileName,
      @Parameter(description = "Новое содержимое файла") String content)
      throws FileNotFoundException;
}
