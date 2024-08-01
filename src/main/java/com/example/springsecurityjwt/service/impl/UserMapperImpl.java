package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.User;
import com.example.springsecurityjwt.entity.UserEntity;
import com.example.springsecurityjwt.service.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity toUserEntity(Register register) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(register.username());
        userEntity.setEmail(register.email());
        userEntity.setPassword(passwordEncoder.encode(register.password()));
        userEntity.setRole(register.role());
        return userEntity;
    }

    @Override
    public User toUser(UserEntity userEntity) {
        return new User(userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());
    }
}
