package com.portfolio_server.dto;

/** JWT issued on successful login. {@code expiresInMs} is the token lifetime. */
public record LoginResponse(String token, String tokenType, long expiresInMs) {

    public static LoginResponse bearer(String token, long expiresInMs) {
        return new LoginResponse(token, "Bearer", expiresInMs);
    }
}