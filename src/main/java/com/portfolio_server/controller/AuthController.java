package com.portfolio_server.controller;

import com.portfolio_server.dto.LoginRequest;
import com.portfolio_server.dto.LoginResponse;
import com.portfolio_server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Public: exchange admin username/password for a JWT. */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) { //LoginRequest는 로그인 요청시 username, password를 body에 넣어서 준다.
        return authService.login(request);
    }
}