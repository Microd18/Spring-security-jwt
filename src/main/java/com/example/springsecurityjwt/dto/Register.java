package com.example.springsecurityjwt.dto;

import com.example.springsecurityjwt.entity.RoleType;
import jakarta.validation.constraints.Size;

public record Register(
        @Size(min = 2, max = 32)
        String username,

        @Size(min = 4, max = 32)
        String email,

        @Size(min = 8, max = 16)
        String password,

        @Size(min = 4, max = 16)
        RoleType role
) {

    public Register {
        if (role == null) {
            role = RoleType.USER;
        }
    }
}
