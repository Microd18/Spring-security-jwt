package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.JWTRequest;
import com.example.springsecurityjwt.dto.JWTResponse;
import com.example.springsecurityjwt.dto.Register;
import lombok.NonNull;

public interface AuthService {

    void register(Register register);

    JWTResponse login(@NonNull JWTRequest authenticationRequest);

    JWTResponse getAccessToken(@NonNull String refreshToken);

    JWTResponse refreshToken(@NonNull String refreshToken);
}
