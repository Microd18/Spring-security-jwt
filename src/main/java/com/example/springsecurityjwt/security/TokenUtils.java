package com.example.springsecurityjwt.security;

import com.example.springsecurityjwt.enums.RoleType;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

/**
 * Утилитарный класс для работы с токенами, включая извлечение информации из токенов.
 */
@Component
public class TokenUtils {
    public TokenAuthentication generate(Claims claims) {
        final TokenAuthentication tokenInfo = new TokenAuthentication();
        tokenInfo.setRole(getRole(claims));
        tokenInfo.setUsername(claims.getSubject());
        return tokenInfo;
    }

    private RoleType getRole(Claims claims) {
        final String role = claims.get("role", String.class);
        return RoleType.valueOf(role);
    }
}
