package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.dto.Register;
import com.example.springsecurityjwt.dto.User;
import com.example.springsecurityjwt.entity.UserEntity;
import com.example.springsecurityjwt.exception.UserNotFoundException;
import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.service.UserMapper;
import com.example.springsecurityjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createUser(Register register) {
        log.info("Вызов метода 'createUser'.");
        userRepository.save(userMapper.toUserEntity(register));
        log.info("Пользователь успешно создан.");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return Optional.of(userMapper.toUser(userEntity));
    }
}

