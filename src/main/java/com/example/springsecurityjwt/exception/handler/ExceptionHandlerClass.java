package com.example.springsecurityjwt.exception.handler;

import com.example.springsecurityjwt.exception.InvalidCredentialsException;
import com.example.springsecurityjwt.exception.InvalidTokenException;
import com.example.springsecurityjwt.exception.UserAlreadyExistsException;
import com.example.springsecurityjwt.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass {
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String invalidCredentialsException(InvalidCredentialsException e){
        return "Неверный логин или пароль";
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String invalidTokenException(InvalidTokenException e){
        return "Недействительный токен";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String userAlreadyExistsException(UserAlreadyExistsException e){
        return "Такой пользователь уже зарегистрирован";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundException(UserNotFoundException e){
        return "Пользователь не найден";
    }
}
