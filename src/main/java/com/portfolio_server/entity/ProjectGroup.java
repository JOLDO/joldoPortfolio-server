package com.portfolio_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 글(Project) 여러 개를 하나로 묶는 단계.
 * 예) 팀 카테고리의 "1차/2차/3차", 회사 카테고리의 "미플/모듈".
 * 화면에서는 카테고리를 누르면 이 그룹들이 가로 스크롤 카드로 나오고,
 * 카드를 누르면 그 그룹에 속한 글들이 파트(웹/앱/서버)별로 보인다.
 */
@Entity
@Table(name = "project_group")
@Getter
@Setter
@NoArgsConstructor
public class ProjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 소속 카테고리(team/personal/company). 글과 마찬가지로 URL 경로에서 받아 저장한다. */
    @Column(nullable = false, length = 20)
    private String category;

    /** 카드에 크게 보일 이름. 예) "1차 프로젝트", "미플" */
    @Column(nullable = false)
    private String name;

    /** 카드에 보일 한 줄 설명. */
    @Column(length = 500)
    private String summary;

    /** 모달에서 보여줄 전체 설명(줄바꿈 포함 긴 글). */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    /**
     * 이 프로젝트에서 내가 맡아 한 일. 줄바꿈으로 여러 줄 적는다.
     * 파트별 상세 글(Project)과 별개로, 모달에서 바로 읽히는 요약 역할.
     */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String contribution;

    /** 진행 기간. 자유 입력이라 문자열로 둔다. 예) "2024.03 ~ 2024.06" */
    @Column(length = 100)
    private String period;

    /** 카드 썸네일 (선택). */
    @Column(nullable = true)
    private String thumbnailUrl;

    /** 공개 여부. false면 방문자에게는 목록/상세에서 안 보인다. */
    @Column(nullable = false, columnDefinition = "boolean not null default true")
    private boolean published;

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
