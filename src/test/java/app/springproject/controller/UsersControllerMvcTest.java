package app.springproject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.springproject.SpringProjectApplication;
import app.springproject.config.SecurityConfig;
import app.springproject.entity.User;
import app.springproject.exception.AuthenticationDataMismatchException;
import app.springproject.exception.UserAlreadyExistsException;
import app.springproject.exception.UserNotFoundException;
import app.springproject.service.UsersService;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsersController.class)
@ContextConfiguration(classes = {SpringProjectApplication.class, SecurityConfig.class})
class UsersControllerMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UsersService usersService;

  private static final User MOCK_USER = new User("Test", "test1");
  private static final String USER_JSON = "{\"username\":\"Test\", \"password\":\"test1\"}";

  @Test
  void shouldSuccessfullyRegisterUser() throws Exception {
    doNothing().when(usersService).registerUser(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signup").contentType("application/json").content(USER_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  void shouldFailRegistration() throws Exception {
    doThrow(UserAlreadyExistsException.class)
        .when(usersService)
        .registerUser(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signup").contentType("application/json").content(USER_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldSuccessfullyAuthenticate() throws Exception {
    doNothing().when(usersService).authenticate(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signin").contentType("application/json").content(USER_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void shouldFailAuthenticationWithNotFound() throws Exception {
    doThrow(UserNotFoundException.class)
        .when(usersService)
        .authenticate(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signin").contentType("application/json").content(USER_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldFailAuthenticationWithUnauthorized() throws Exception {
    doThrow(AuthenticationDataMismatchException.class)
        .when(usersService)
        .authenticate(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signin").contentType("application/json").content(USER_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldSuccessfullyFindUser() throws Exception {
    when(usersService.getByUsername(any(String.class))).thenReturn(MOCK_USER);
    mockMvc
        .perform(get("/second-memory/Test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  void shouldFailToFindUser() throws Exception {
    when(usersService.getByUsername(any(String.class))).thenThrow(UserNotFoundException.class);
    mockMvc.perform(get("/second-memory/Test")).andExpect(status().isNotFound());
  }

  @Test
  void shouldSuccessfullyDeleteUser() throws Exception {
    when(usersService.deleteUser(any(String.class))).thenReturn(MOCK_USER);
    mockMvc
        .perform(delete("/second-memory/delete/Test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  void shouldFailToDeleteUser() throws Exception {
    when(usersService.deleteUser(any(String.class))).thenThrow(UserNotFoundException.class);
    mockMvc.perform(delete("/second-memory/delete/Test")).andExpect(status().isNotFound());
  }

  @Test
  void shouldSuccessfullyUpdateUser() throws Exception {
    doNothing().when(usersService).updateUser(any(User.class));
    mockMvc
        .perform(patch("/second-memory/update").contentType("application/json").content(USER_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  void shouldFailToUpdateUser() throws Exception {
    doThrow(UserNotFoundException.class).when(usersService).updateUser(any(User.class));
    mockMvc
        .perform(patch("/second-memory/update").contentType("application/json").content(USER_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldSuccessfullyReturnUsers() throws Exception {
    ArrayList<String> mockList = new ArrayList<>(Arrays.asList("Test", "Test"));
    when(usersService.getAll()).thenReturn(mockList);
    mockMvc.perform(get("/second-memory/main")).andExpect(status().isOk());
  }
}
