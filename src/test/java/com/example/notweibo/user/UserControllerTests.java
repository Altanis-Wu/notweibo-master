package com.example.notweibo.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    List<User> defaultUsers = List.of(
            new User(1L,
                    "Ann",
                    "ann@hotmail.com",
                    LocalDate.of(1970, 1, 1),
                    new ArrayList<>(),
                    new ArrayList<>()),
            new User(2L,
                    "Bob",
                    "bob@hotmail.com",
                    LocalDate.of(1978, 1, 1),
                    new ArrayList<>(),
                    new ArrayList<>())
    );

    @Test
    public void testGetUserSuccess() throws Exception{
        when(userService.getUser(1L)).thenReturn(defaultUsers.get(0));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserNotFound() throws Exception{
        when(userService.getUser(1L)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRegisterNewUser() throws Exception{
        String json = "{\"id\": 1, \"name\": \"Ann\", \"email\": \"ann@hotmail.com\", \"dateOfBirth\": " +
                "\"1970-01-01\", \"posts\": [], " +
                "\"likedPosts\": []}";

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/users")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(userService).addNewUser(any(User.class));
    }

    @Test
    public void testRegisterNewUserAlreadyInUse() throws Exception{
        String json = "{\"id\": 1, \"name\": \"Ann\", \"email\": \"ann@hotmail.com\", \"dateOfBirth\": " +
                "\"1970-01-01\", \"posts\": [], " +
                "\"likedPosts\": []}";


        doThrow(UserEmailAlreadyInUseException.class)
                .when(userService)
                .addNewUser(any(User.class));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/users")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

        verify(userService).addNewUser(any(User.class));
    }
}
