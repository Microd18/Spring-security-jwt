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
/**
 * Глобальный обработчик исключений для контроллеров.
 * Используется для обработки исключений, возникающих в процессе работы приложения,
 * и возвращения соответствующих HTTP статусов и сообщений об ошибках.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Неверный логин или пароль");
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidTokenException(InvalidTokenException e) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Недействительный токен");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), "Такой пользователь уже зарегистрирован");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Пользователь не найден");
    }
}
