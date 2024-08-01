package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.User;
import com.example.springsecurityjwt.entity.UserEntity;

public interface UserMapper {

    UserEntity toUserEntity(Register register);

    User toUser(UserEntity userEntity);
}
