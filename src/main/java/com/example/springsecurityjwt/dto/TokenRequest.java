package com.example.springsecurityjwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record TokenRequest(
        @Size(min = 4, max = 32)
        @Email
        String email,
        @Size(min = 8, max = 16)
        String password) {

}
