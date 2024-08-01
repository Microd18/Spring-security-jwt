package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.TokenRequest;
import com.example.springsecurityjwt.dto.TokenResponse;
import com.example.springsecurityjwt.dto.User;
import com.example.springsecurityjwt.exception.InvalidCredentialsException;
import com.example.springsecurityjwt.exception.InvalidTokenException;
import com.example.springsecurityjwt.exception.UserAlreadyExistsException;
import com.example.springsecurityjwt.exception.UserNotFoundException;
import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.security.TokenProvider;
import com.example.springsecurityjwt.service.AuthService;
import com.example.springsecurityjwt.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(Register register) {

        if (userRepository.existsByEmail(register.email())) {
            throw new UserAlreadyExistsException();
        } else {
            userService.createUser(register);
            log.info("Пользователь успешно зарегистрирован.");
        }
    }

    @Override
    public TokenResponse login(@NonNull TokenRequest authenticationRequest) {
        final User user = userService.getByEmail(authenticationRequest.email())
                .orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(authenticationRequest.password(), user.password())) {
            final String accessToken = tokenProvider.generateAccessToken(user);
            final String refreshToken = tokenProvider.generateRefreshToken(user);
            refreshStorage.put(user.email(), refreshToken);
            log.info("Пользователь успешно вошел в систему.");
            return new TokenResponse(accessToken, refreshToken);
        } else {
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public TokenResponse getAccessToken(@NonNull String refreshToken) {
        if (tokenProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = tokenProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(UserNotFoundException::new);
                final String accessToken = tokenProvider.generateAccessToken(user);
                log.info("Новый 'access' токен получен.");
                return new TokenResponse(accessToken, "");
            }
        }
        throw new InvalidTokenException();
    }

    @Override
    public TokenResponse refreshToken(@NonNull String refreshToken) {
        if (tokenProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = tokenProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(UserNotFoundException::new);
                final String accessToken = tokenProvider.generateAccessToken(user);
                final String newRefreshToken = tokenProvider.generateRefreshToken(user);
                refreshStorage.put(user.email(), newRefreshToken);
                log.info("Новые 'access' и 'refresh' токены получены.");
                return new TokenResponse(accessToken, newRefreshToken);
            }
            log.info("Неверный токен.");
        }
        throw new InvalidTokenException();
    }
}

