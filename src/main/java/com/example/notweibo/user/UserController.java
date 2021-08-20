package com.example.notweibo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path="/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(
            UserService userService) {
        this.userService = userService;
    }

    // UserController.java
    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(path="{userId}")
    public User getUser(
            @PathVariable("userId") Long id){
        return userService.getUser(id);
    }

    @PostMapping
    public void registerNewUser(
            @RequestBody User newUser){
        userService.addNewUser(newUser);
    }

    @DeleteMapping(path="{userId}")
    public void deleteUser(
            @PathVariable("userId") Long id){
        userService.deleteUser(id);
    }

    @PutMapping(path="{userId}")
    public void updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody User updatedUser){
        userService.updateUser(
                userId, updatedUser);
    }
}
