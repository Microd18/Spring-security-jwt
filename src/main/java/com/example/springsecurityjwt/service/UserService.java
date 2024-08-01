package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.User;

import java.util.Optional;

public interface UserService {

    void createUser(Register register);
    Optional<User> getByEmail(String email);
}
