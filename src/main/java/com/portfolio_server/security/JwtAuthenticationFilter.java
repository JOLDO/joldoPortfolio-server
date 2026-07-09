package com.portfolio_server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Reads {@code Authorization: Bearer <token>}, verifies the JWT, loads the user from the DB,
 * and populates the security context so protected endpoints see an authenticated user
 * with the roles they currently have.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;    //토큰 검증
    private final CustomUserDetailsService userDetailsService;  //DB에서 회원 로드

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);   //요청에서 Authorization의 설정 값 : "Authorization": `Bearer ${token}`
        if (header != null && header.startsWith(BEARER_PREFIX)
                && SecurityContextHolder.getContext().getAuthentication() == null) {    //요청에 Authorization의 설정 값있는지&&Bearer 가 붙어있는지&&아직 인증정보가 없는지
            String token = header.substring(BEARER_PREFIX.length());    //토큰 값만 받아옴
            try {
                String username = jwtService.validateAndGetSubject(token);  //토큰이 정상인지 확인하고 토큰이 누구것인지 가져옴
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);  //유저의 이름으로 유저의 세부 내용을 가져옴
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());   //유저정보, 비밀번호(null), 권한을 객체로 만들고
                SecurityContextHolder.getContext().setAuthentication(authentication);   //유저정보, 비밀번호(null), 권한을 인증정보에 등록
            } catch (Exception ignored) {
                // Invalid/expired token or unknown user: leave the request unauthenticated.
            }
        }
        filterChain.doFilter(request, response);
    }
}