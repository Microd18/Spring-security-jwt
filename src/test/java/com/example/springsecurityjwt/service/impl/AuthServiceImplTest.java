package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.TokenRequest;
import com.example.springsecurityjwt.dto.TokenResponse;
import com.example.springsecurityjwt.dto.User;
import com.example.springsecurityjwt.enums.RoleType;
import com.example.springsecurityjwt.exception.UserAlreadyExistsException;
import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    @DisplayName("Проверка успешной регистрации пользователя")
    void registerTest() {
        Register register = new Register("username", "username@gmail.com", "password", RoleType.USER);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        authService.register(register);

        verify(userRepository, times(1)).existsByEmail(register.email());
        verify(userService, times(1)).createUser(any(Register.class));
    }

    @Test
    @DisplayName("Проверка выброса исключения, если пользователь уже существует")
    void registerExistingUserTest() {
        Register register = new Register("username", "username@gmail.com", "password", RoleType.USER);

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(register));

        verify(userRepository, times(1)).existsByEmail(register.email());
        verify(userService, times(0)).createUser(any(Register.class));
    }

    @Test
    @DisplayName("Проверка успешного логина пользователя")
    void loginTest() {
        TokenRequest tokenRequest = new TokenRequest("user@gmail.com", "password");

        User user = new User("username", "user@gmail.com", "password", RoleType.USER);

        TokenResponse tokenResponse = new TokenResponse(
                "0JjQstCw0L3QvtCyINCS0LjQutGC0L7RgCDQktC40LrRgtC+0YDQvtCy0LjRhw==",
                "0KDQvtGB0YHQuNGPLCDQntGA0LXQvdCx0YPRgNCzLCDQn9C70LDRgtC+0Lo="
        );

        when(userService.getByEmail(tokenRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(tokenRequest.password(), user.password())).thenReturn(true);
        when(tokenProvider.generateAccessToken(user)).thenReturn(tokenResponse.accessToken());
        when(tokenProvider.generateRefreshToken(user)).thenReturn(tokenResponse.refreshToken());

        TokenResponse actual = authService.login(tokenRequest);

        assertEquals(tokenResponse.accessToken(), actual.accessToken());
        assertEquals(tokenResponse.refreshToken(), actual.refreshToken());

        verify(tokenProvider, times(1)).generateAccessToken(user);
        verify(tokenProvider, times(1)).generateRefreshToken(user);
    }
}