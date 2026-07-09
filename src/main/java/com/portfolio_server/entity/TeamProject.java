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

@Entity
@Table(name = "team_project")
@Getter
@Setter
@NoArgsConstructor
public class TeamProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    /** Short description shown on the list card. */
    @Column(length = 500)
    private String summary;

    /**
     {
        "type": "doc",
        "content": [
            { "type": "paragraph", "content": [{ "type": "text", "text": "안녕하세요" }] },
            { "type": "image", "attrs": { "src": "http://localhost:8080/uploads/abc.png" } }
        ]
     }형식의 json으로 들어감
     */
    //TipTap은 ProseMorror엔진에서 돌아가는 React용 리치 텍스트 편집기(글자 굵기/ 기울기, 이미지, 목록같을걸 할수 있는 편집기)
    @Lob    //긴 텍스트나 대용량 데이터
    @Column(columnDefinition = "LONGTEXT")  //이미지와 설명이 들어갈 본문 내용이라 LONGTEXT로 해줌
    private String content;

    /** URL of the representative image for the list card (optional). */
    @Column(nullable = true)
    private String thumbnailUrl;

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