package com.example.notweibo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    List<User> defaultUsers = List.of(
            new User(1L, "Ann", "ann@hotmail.com", LocalDate.of(1970, 1, 1), new ArrayList<>(), new ArrayList<>()),
            new User(2L, "Bob", "bob@hotmail.com", LocalDate.of(1978, 1, 1), new ArrayList<>(), new ArrayList<>()));

    @BeforeEach
    public void bootstrapDatabase() {
        userRepository.deleteAll();
        userRepository.saveAll(defaultUsers);
    }

    @Test
    public void testSaveReadDeleteAll() {
        userRepository.deleteAll();
        List<User> users = userRepository.findAll();
        assertEquals(0, users.size());

        userRepository.saveAll(defaultUsers);

        List<User> savedUsers = userRepository.findAll();
        assertEquals(defaultUsers.size(), savedUsers.size());
    }

    @Test
    public void testFindByEmailSuccess() {
        Optional<User> bob = userRepository.findUserByEmail("bob@hotmail.com");
        assertTrue(bob.isPresent());
    }

    @Test
    public void testFindByEmailNotFound() {
        Optional<User> emma = userRepository.findUserByEmail("emma@hotmail.com");
        assertFalse(emma.isPresent());
    }

}
