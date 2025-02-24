package com.example.springsecurityjwt.security;

import com.example.springsecurityjwt.dto.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Класс для генерации и валидации JWT токенов.
 */
@Component
@Slf4j
public class TokenProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public TokenProvider(@Value("${jwt.secret.access}") String jwtAccessSecret,
                         @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstantTime = now.plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpirationDate = Date.from(accessExpirationInstantTime);
        return Jwts.builder()
                .setSubject(user.email())
                .setExpiration(accessExpirationDate)
                .signWith(jwtAccessSecret)
                .claim("role", user.role().name())
                .claim("username", user.username())
                .compact();
    }

    public String generateRefreshToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstantTime = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpirationDate = Date.from(refreshExpirationInstantTime);
        return Jwts.builder()
                .setSubject(user.email())
                .setExpiration(refreshExpirationDate)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported json web token", e);
        } catch (MalformedJwtException e) {
            log.error("Malformed json web token");
        } catch (SignatureException e) {
            log.error("Invalid signature", e);
        } catch (Exception e) {
            log.error("Invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
