package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        User user = new User(null, "John", "john@example.com");
        UserDto userDto = new UserDto(1L, "John", "john@example.com");

        when(userService.createUser(any(User.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    public void getUser_ShouldReturnUser() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "Jane", "jane@example.com");

        when(userService.getUser(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    public void updateUser_ShouldReturnUpdatedUser() throws Exception {
        Long userId = 2L;
        User updateUser = new User(null, "UpdatedName", "updated@example.com");
        UserDto updatedDto = new UserDto(userId, "UpdatedName", "updated@example.com");

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    public void deleteUser_ShouldReturnOk() throws Exception {
        Long userId = 3L;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        // Можно дополнительно проверить вызов сервиса
        Mockito.verify(userService).deleteUser(userId);
    }
}