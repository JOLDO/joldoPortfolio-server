package com.portfolio_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Application user. Password is stored as a BCrypt hash, never in plaintext. */
@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor  //JPA는 객체생성시 기본 생성자가 반드시 필요해서 넣음
public class Member {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto incerease
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    /** BCrypt hash of the password. */
    @Column(nullable = false)
    private String password;

    /** Role name without the {@code ROLE_} prefix, e.g. {@code ADMIN}. */
    @Column(nullable = false)
    private String role;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist //처음 저장 직전 한번만 실행
    //Instant.now : 시간의 한순간, 지금
    void onCreate() {
        this.createdAt = Instant.now();
    }
}