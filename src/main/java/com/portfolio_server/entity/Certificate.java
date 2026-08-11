package com.portfolio_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 자격증. 메인 화면 05 섹션에 카드로 나온다. */
@Entity
@Table(name = "certificate")
@Getter
@Setter
@NoArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 자격증 이름. 예) 정보처리기사 */
    @Column(nullable = false)
    private String name;

    /** 발급기관. 예) 한국산업인력공단 */
    @Column(length = 100)
    private String issuer;

    /**
     * 취득일. "2018.08" 처럼 자유롭게 적을 수 있게 문자열로 둔다.
     * 정렬도 이 값 기준이라 "YYYY.MM" 형식으로 통일해서 넣는 게 좋다.
     */
    @Column(length = 20)
    private String acquiredDate;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
