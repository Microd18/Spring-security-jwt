package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.TokenRequest;
import com.example.springsecurityjwt.dto.TokenResponse;
import com.example.springsecurityjwt.dto.Register;
import lombok.NonNull;

public interface AuthService {

    void register(Register register);

    TokenResponse login(@NonNull TokenRequest authenticationRequest);

    TokenResponse getAccessToken(@NonNull String refreshToken);

    TokenResponse refreshToken(@NonNull String refreshToken);
}
