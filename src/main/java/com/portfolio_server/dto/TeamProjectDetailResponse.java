package com.portfolio_server.dto;

import com.portfolio_server.entity.TeamProject;
import java.time.Instant;

/** Full view for the detail page, including the editor content body. */
public record TeamProjectDetailResponse(
        Long id,
        String title,
        String summary,
        String content,
        String thumbnailUrl,
        Instant createdAt,
        Instant updatedAt) {

    public static TeamProjectDetailResponse from(TeamProject p) {
        return new TeamProjectDetailResponse(
                p.getId(),
                p.getTitle(),
                p.getSummary(),
                p.getContent(),
                p.getThumbnailUrl(),
                p.getCreatedAt(),
                p.getUpdatedAt());
    }
}