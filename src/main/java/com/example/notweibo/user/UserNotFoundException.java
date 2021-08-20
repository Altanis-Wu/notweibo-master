package com.example.notweibo.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends IllegalArgumentException{
    private final Long userId;

    public UserNotFoundException(Long userId) {
        super("User with Id " + userId +  " not found.");
        this.userId = userId;
    }
}
