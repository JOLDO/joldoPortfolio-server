package com.portfolio_server.dto;

/** Admin login credentials. */
public record LoginRequest(String username, String password) {
}