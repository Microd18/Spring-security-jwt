package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.dto.RefreshTokenRequest;
import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.TokenRequest;
import com.example.springsecurityjwt.dto.TokenResponse;
import com.example.springsecurityjwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody Register register) {
        authService.register(register);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest authRequest) {
        final TokenResponse tokenResponse = authService.login(authRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/access")
    public ResponseEntity<TokenResponse> getNewAccessToken(@RequestBody RefreshTokenRequest request) {
        final TokenResponse tokenResponse = authService.getAccessToken(request.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> getNewRefreshToken(@RequestBody RefreshTokenRequest request) {
        final TokenResponse tokenResponse = authService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }
}

