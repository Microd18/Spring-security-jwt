package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.dto.JWTRequest;
import com.example.springsecurityjwt.dto.JWTResponse;
import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.User;
import com.example.springsecurityjwt.entity.RoleType;
import com.example.springsecurityjwt.exception.InvalidCredentialsException;
import com.example.springsecurityjwt.exception.InvalidTokenException;
import com.example.springsecurityjwt.exception.UserAlreadyExistsException;
import com.example.springsecurityjwt.exception.UserNotFoundException;
import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.security.JWTProvider;
import com.example.springsecurityjwt.service.AuthService;
import com.example.springsecurityjwt.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.HashMap;
import java.util.Map;

import static com.example.springsecurityjwt.entity.RoleType.USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JWTProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(Register register) {

        if (userRepository.existsByEmail(register.email())) {
            throw new UserAlreadyExistsException(); //пользователь уже существует
        }
        userService.createUser(register);
        log.info("User was successfully registered.");
    }

    @Override
    public JWTResponse login(@NonNull JWTRequest authenticationRequest) {
        final User user = userService.getByEmail(authenticationRequest.email())
                .orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(authenticationRequest.password(), user.password())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.email(), refreshToken);
            log.info("User was successfully logged in");
            return new JWTResponse(accessToken, refreshToken);
        } else {
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public JWTResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(UserNotFoundException::new);
                final String accessToken = jwtProvider.generateAccessToken(user);
                log.info("New access token was successfully got");
                return new JWTResponse(accessToken, "");
            }
        }
        throw new InvalidTokenException();
    }
    @Override
    public JWTResponse refreshToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(UserNotFoundException::new);
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.email(), newRefreshToken);
                log.info("New access and refresh tokens was successfully got");
                return new JWTResponse(accessToken, newRefreshToken);
            }
            log.info("Tokens are not equal.");
        }
        throw new InvalidTokenException();
    }
}

