package com.portfolio_server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Issues and verifies HS256-signed JWTs for the admin. */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms:3600000}") long expirationMs) {
        // HS256 requires a key of at least 256 bits (32 bytes).
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); //jwt시크릿을 utf-8f로 비밀키 만듦
        this.expirationMs = expirationMs;   //설정한 만료시간 읽어옴
    }

    /** Creates a token whose subject is the given username. */
    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)  //토큰 주인
                .issuedAt(now)  //만든 시간
                .expiration(new Date(now.getTime() + expirationMs)) //만료시간
                .signWith(key)  //비밀키로 서명
                .compact(); //jwt토큰 만듦
    }

    /**
     * Verifies the signature/expiry and returns the subject (username).
     * Throws {@link io.jsonwebtoken.JwtException} if the token is invalid or expired.
     */
    public String validateAndGetSubject(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)    //검증할 비밀키 지정
                .build()
                .parseSignedClaims(token)   //토큰 검증 후 해석
                .getPayload();  //claims을 꺼냄
        return claims.getSubject(); //해석한 claims에서 주인을 반환
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}