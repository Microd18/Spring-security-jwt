package com.example.springsecurityjwt.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ErrorResponse {
    private int statusCode;
    private String message;
}
