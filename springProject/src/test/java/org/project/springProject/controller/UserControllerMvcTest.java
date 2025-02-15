package org.project.springProject.controller;

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

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.project.springProject.entity.User;
import org.project.springProject.exception.AuthenticationDataMismatchException;
import org.project.springProject.exception.UserAlreadyExistsException;
import org.project.springProject.exception.UserNotFoundException;
import org.project.springProject.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  private static final User MOCK_USER = new User("Test", "test1");
  private static final String USER_JSON = "{\"username\":\"Test\", \"password\":\"test1\"}";

  @Test
  public void shouldSuccessfullyRegisterUser() throws Exception {
    doNothing().when(userService).registerUser(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signup").contentType("application/json").content(USER_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  public void shouldFailRegistration() throws Exception {
    doThrow(UserAlreadyExistsException.class)
        .when(userService)
        .registerUser(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signup").contentType("application/json").content(USER_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldSuccessfullyAuthenticate() throws Exception {
    doNothing().when(userService).authenticate(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signin").contentType("application/json").content(USER_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldFailAuthenticationWithNotFound() throws Exception {
    doThrow(UserNotFoundException.class)
        .when(userService)
        .authenticate(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signin").contentType("application/json").content(USER_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldFailAuthenticationWithUnauthorized() throws Exception {
    doThrow(AuthenticationDataMismatchException.class)
        .when(userService)
        .authenticate(any(String.class), any(String.class));
    mockMvc
        .perform(post("/second-memory/signin").contentType("application/json").content(USER_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldSuccessfullyFindUser() throws Exception {
    when(userService.getByUsername(any(String.class))).thenReturn(MOCK_USER);
    mockMvc
        .perform(get("/second-memory/Test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  public void shouldFailToFindUser() throws Exception {
    when(userService.getByUsername(any(String.class))).thenThrow(UserNotFoundException.class);
    mockMvc.perform(get("/second-memory/Test")).andExpect(status().isNotFound());
  }

  @Test
  public void shouldSuccessfullyDeleteUser() throws Exception {
    when(userService.deleteUser(any(String.class))).thenReturn(MOCK_USER);
    mockMvc
        .perform(delete("/second-memory/delete/Test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  public void shouldFailToDeleteUser() throws Exception {
    when(userService.deleteUser(any(String.class))).thenThrow(UserNotFoundException.class);
    mockMvc.perform(delete("/second-memory/delete/Test")).andExpect(status().isNotFound());
  }

  @Test
  public void shouldSuccessfullyUpdateUser() throws Exception {
    doNothing().when(userService).updateUser(any(User.class));
    mockMvc
        .perform(patch("/second-memory/update").contentType("application/json").content(USER_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Test"))
        .andExpect(jsonPath("$.password").value("test1"));
  }

  @Test
  public void shouldFailToUpdateUser() throws Exception {
    doThrow(UserNotFoundException.class).when(userService).updateUser(any(User.class));
    mockMvc
        .perform(patch("/second-memory/update").contentType("application/json").content(USER_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldSuccessfullyReturnUsers() throws Exception {
    ArrayList<String> mockList = new ArrayList<String>(Arrays.asList("Test", "Test"));
    when(userService.getAll()).thenReturn(mockList);
    mockMvc.perform(get("/second-memory/main")).andExpect(status().isOk());
  }
}
