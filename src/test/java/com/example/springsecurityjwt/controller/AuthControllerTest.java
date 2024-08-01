package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.dto.RefreshTokenRequest;
import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.TokenRequest;
import com.example.springsecurityjwt.dto.TokenResponse;
import com.example.springsecurityjwt.enums.RoleType;
import com.example.springsecurityjwt.exception.UserAlreadyExistsException;
import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.security.TokenProvider;
import com.example.springsecurityjwt.security.TokenUtils;
import com.example.springsecurityjwt.security.config.SecurityConfig;
import com.example.springsecurityjwt.service.impl.AuthServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenUtils tokenUtils;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private AuthServiceImpl authService;
    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Проверка регистрации нового юзера")
    void registerTest() throws Exception {
        Register register = new Register("username", "user@gmail.com", "password", RoleType.USER);

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(register))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(authService, times(1)).register((any(Register.class)));
    }

    @SneakyThrows
    @Test
    @DisplayName("Проверка выброса исключения, если пользователь уже существует")
    void registerUserAlreadyExistsExceptionTest() {
        Register register = new Register("username", "user@gmail.com", "password", RoleType.USER);

        doThrow(new UserAlreadyExistsException()).when(authService).register(any(Register.class));

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(register))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()) // Ожидаем HTTP статус 409 CONFLICT
                .andExpect(content().json("{\"statusCode\":409,\"message\":\"Такой пользователь уже зарегистрирован\"}")); // Ожидаем сообщение об ошибке
    }

    @SneakyThrows
    @Test
    @DisplayName("Проверка получения нового токена, когда он стал недействительным")
    @WithMockUser
    void getNewAccessTokenTest() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("0KDQvtGB0YHQuNGPLCDQntGA0LXQvdCx0YPRgNCzLCDQn9C70LDRgtC+0Lo=");

        TokenResponse tokenResponse = new TokenResponse(
                "0JjQstCw0L3QvtCyINCS0LjQutGC0L7RgCDQktC40LrRgtC+0YDQvtCy0LjRhw==",
                "0KDQvtGB0YHQuNGPLCDQntGA0LXQvdCx0YPRgNCzLCDQn9C70LDRgtC+0Lo="
        );

        when(authService.refreshToken(refreshTokenRequest.refreshToken())).thenReturn(tokenResponse);

        mvc.perform(post("/auth/refresh")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.refreshToken").value(tokenResponse.refreshToken()));

        verify(authService, times(1)).refreshToken(any(String.class));
    }

    @SneakyThrows
    @Test
    @DisplayName("Проверка авторизации юзера")
    void loginTest() {
        TokenRequest tokenRequest = new TokenRequest("user@gmail.com", "password");

        TokenResponse tokenResponse = new TokenResponse(
                "0JjQstCw0L3QvtCyINCS0LjQutGC0L7RgCDQktC40LrRgtC+0YDQvtCy0LjRhw==",
                "0KDQvtGB0YHQuNGPLCDQntGA0LXQvdCx0YPRgNCzLCDQn9C70LDRgtC+0Lo=");

        when(authService.login(tokenRequest)).thenReturn(tokenResponse);

        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(tokenRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.refreshToken").value(tokenResponse.refreshToken()));

        verify(authService, times(1)).login(any(TokenRequest.class));
    }


}