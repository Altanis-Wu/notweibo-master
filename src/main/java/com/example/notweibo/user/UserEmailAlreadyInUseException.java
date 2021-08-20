package com.example.notweibo.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserEmailAlreadyInUseException extends IllegalArgumentException{
    private final String email;

    public UserEmailAlreadyInUseException(String email) {
        super("User with email " + email + " already exists.");
        this.email = email;
    }
}
