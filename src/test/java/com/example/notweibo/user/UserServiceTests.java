package com.example.notweibo.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    List<User> defaultUsers = List.of(
            new User(1L, "Ann", "ann@hotmail.com", LocalDate.of(1970, 1, 1), new ArrayList<>(), new ArrayList<>()),
            new User(2L, "Bob", "bob@hotmail.com", LocalDate.of(1978, 1, 1), new ArrayList<>(), new ArrayList<>()));

    @Test
    public void testGetUsers() {

        when(userRepository.findAll()).thenReturn(defaultUsers);

        List<User> users = userService.getUsers();
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    public void testGetUserSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUsers.get(0)));
        User user = userService.getUser(1L);
        assertNotNull(user);
    }

    @Test
    public void testGetUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(0L));
    }

    @Test
    public void testAddNewUser() {
        User newUser = new User(3L, "Conner", "conner@hotmail.com", LocalDate.of(1978, 1, 1), new ArrayList<>(),
                new ArrayList<>());

        userService.addNewUser(newUser);
        verify(userRepository).save(newUser);
    }
}
