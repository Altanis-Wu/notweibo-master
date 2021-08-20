package com.example.notweibo;

import com.example.notweibo.post.Post;
import com.example.notweibo.post.PostRepository;
import com.example.notweibo.user.User;
import com.example.notweibo.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class NotweiboApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotweiboApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository) {
        return args -> {
            List<User> users = List.of(
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

            userRepository.saveAll(users);
        };
    }


    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository, PostRepository postRepository) {
        return args -> {
            List<User> users = List.of(
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

            List<User> savedUsers = userRepository.saveAll(users);

            List<Post> posts = List.of(
                    new Post(
                            "First post!",
                            savedUsers.get(0),
                            List.of(
                                    savedUsers.get(0),
                                    savedUsers.get(1)
                            )
                    ),
                    new Post("Going to the cinema.",
                            savedUsers.get(0),
                            List.of(
                                    savedUsers.get(0),
                                    savedUsers.get(1)
                            )
                    ),
                    new Post("Boba tea!",
                            savedUsers.get(1),
                            List.of(
                                    savedUsers.get(0),
                                    savedUsers.get(1)
                            )
                    )
            );

            postRepository.saveAll(posts);
        };
    }
}
