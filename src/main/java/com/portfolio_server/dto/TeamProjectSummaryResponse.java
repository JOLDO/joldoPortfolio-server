package com.portfolio_server.dto;

import com.portfolio_server.entity.TeamProject;
import java.time.Instant;

/** Lightweight view for the list page (no full content body). */
public record TeamProjectSummaryResponse(
        Long id,
        String title,
        String summary,
        String thumbnailUrl,
        Instant createdAt,
        Instant updatedAt) {

    public static TeamProjectSummaryResponse from(TeamProject p) {
        return new TeamProjectSummaryResponse(
                p.getId(),
                p.getTitle(),
                p.getSummary(),
                p.getThumbnailUrl(),
                p.getCreatedAt(),
                p.getUpdatedAt());
    }
}