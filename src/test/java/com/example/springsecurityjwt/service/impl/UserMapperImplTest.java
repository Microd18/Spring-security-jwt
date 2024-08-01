package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.User;
import com.example.springsecurityjwt.entity.UserEntity;
import com.example.springsecurityjwt.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final UserEntity expected = new UserEntity();
    final Register register = new Register("Viktor", "Viktor@gmail.com", "password", RoleType.USER);

    @BeforeEach
    public void setUp() {
        expected.setId(1L);
        expected.setUsername("Viktor");
        expected.setEmail("Viktor@gmail.com");
        expected.setPassword("password");
        expected.setRole(RoleType.USER);
    }

    @Test
    void shouldProperlyMapRegisterToUserEntity() {

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        UserEntity userEntity = userMapper.toUserEntity(register);

        assertNotNull(userEntity);
        assertEquals(register.username(), userEntity.getUsername());
        assertEquals(register.email(), userEntity.getEmail());
        assertEquals(register.password(), userEntity.getPassword());
        assertEquals(register.role(), userEntity.getRole());
    }

    @Test
    void shouldProperlyMapUserEntityToUser() {

        User user = userMapper.toUser(expected);

        assertNotNull(user);
        assertEquals(expected.getUsername(), user.username());
        assertEquals(expected.getEmail(), user.email());
        assertEquals(expected.getPassword(), user.password());
        assertEquals(expected.getRole(), user.role());
    }
}