package com.example.springsecurityjwt.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

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
