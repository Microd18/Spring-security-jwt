package com.example.springsecurityjwt.security.config;

import com.example.springsecurityjwt.security.filter.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности для приложения Spring Security.
 * Настраивает цепочку фильтров безопасности, управление сессиями и шифрование паролей.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic((AbstractHttpConfigurer::disable))
                .csrf((AbstractHttpConfigurer::disable))
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/auth/registration", "/auth/login", "/auth/token",
                                        "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
