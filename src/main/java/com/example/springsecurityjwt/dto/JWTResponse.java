package com.example.springsecurityjwt.dto;

public record JWTResponse(
        String accessToken,
        String refreshToken) {

    private static final String TOKEN_TYPE = "Bearer";

    public String getTokenType() {
        return TOKEN_TYPE;
    }
}
