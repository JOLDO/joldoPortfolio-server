package com.portfolio_server.service;

import com.portfolio_server.dto.LoginRequest;
import com.portfolio_server.dto.LoginResponse;
import com.portfolio_server.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Authenticates credentials against the DB (via AuthenticationManager) and issues a JWT. */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate( //이건 로그인시 유저이름과 비번으로 정보가 맞는지 확인
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            String token = jwtService.generateToken(authentication.getName());  //로그인 성공시 이름을 이용해 토큰 생성
            return LoginResponse.bearer(token, jwtService.getExpirationMs());   //토큰값과 만료시간을 Bearer타입 응답으로 반환
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}