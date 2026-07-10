package com.portfolio_server.dto;

/**
 * Payload for creating/updating a project (admin only). Category comes from the URL, not here.
 * published: true=공개, false=작성 중(비공개). 값이 없으면(null) 공개로 처리한다.
 */
public record ProjectRequest(
        String title,
        String summary,
        String content,
        String thumbnailUrl,
        Boolean published) {
}