package ru.practicum.shareit.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.validator.ValidatorUser;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidatorUser validator;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "John", "john@example.com");
    }

    @Test
    public void createUserShouldSaveAndReturnDto() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        User userToCreate = new User(null, "John", "john@example.com");

        UserDto result = userService.createUser(userToCreate);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());

        verify(validator).validMail(eq(userToCreate), any());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void getUserShouldReturnExisting() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDto result = userService.getUser(1L);

        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());

        verify(userRepository).findById(1L);
    }

    @Test
    public void getUserShouldThrowNotFoundException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.getUser(2L);
        });

        verify(userRepository).findById(2L);
    }

    @Test
    public void updateUserShouldUpdateFields() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updateData = new User(null, "NewName", "newemail@example.com");

        UserDto result = userService.updateUser(1L, updateData);

        assertEquals("NewName", result.getName());
        assertEquals("newemail@example.com", result.getEmail());

        verify(validator).validMail(eq(updateData), any());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void deleteUserShouldCallDeleteById() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(5L);

        verify(userRepository).deleteById(5L);
    }
}