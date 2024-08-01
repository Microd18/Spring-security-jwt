package com.example.springsecurityjwt.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление, представляющее типы ролей в системе.
 * Реализует интерфейс {@link GrantedAuthority} для использования в Spring Security.
 */
@RequiredArgsConstructor
public enum RoleType implements GrantedAuthority {
    ADMIN("ADMIN"),
    USER("USER");

    private final String roleValue;

    @Override
    public String getAuthority() {
        return "ROLE_" + roleValue;
    }
}
