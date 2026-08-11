package com.portfolio_server.dto;

import com.portfolio_server.entity.ProjectGroup;
import java.time.Instant;

/** 가로 스크롤 카드용 가벼운 뷰 (긴 설명 제외). */
public record ProjectGroupSummaryResponse(
        Long id,
        String name,
        String summary,
        String period,
        String thumbnailUrl,
        boolean published,
        Instant createdAt,
        Instant updatedAt) {

    public static ProjectGroupSummaryResponse from(ProjectGroup g) {
        return new ProjectGroupSummaryResponse(
                g.getId(),
                g.getName(),
                g.getSummary(),
                g.getPeriod(),
                g.getThumbnailUrl(),
                g.isPublished(),
                g.getCreatedAt(),
                g.getUpdatedAt());
    }
}
