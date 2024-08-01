package com.example.springsecurityjwt.dto;

import com.example.springsecurityjwt.enums.RoleType;

public record User(
        String username,
        String email,
        String password,
        RoleType role) {
}
