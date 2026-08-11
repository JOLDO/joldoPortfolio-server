package com.portfolio_server.dto;

/**
 * 프로젝트 그룹 생성/수정 payload (관리자 전용). 카테고리는 URL에서 받으므로 여기 없다.
 * published: true=공개, false=작성 중(비공개). 값이 없으면(null) 공개로 처리한다.
 */
public record ProjectGroupRequest(
        String name,
        String summary,
        String description,
        String contribution,
        String period,
        String thumbnailUrl,
        Boolean published) {
}
