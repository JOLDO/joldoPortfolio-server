package com.portfolio_server.dto;

import com.portfolio_server.entity.Project;
import java.time.Instant;

/** Lightweight view for the list page (no full content body). */
public record ProjectSummaryResponse(
        Long id,
        Long groupId,
        String part,
        String title,
        String summary,
        String thumbnailUrl,
        boolean published,
        Instant createdAt,
        Instant updatedAt) {

    public static ProjectSummaryResponse from(Project p) {
        return new ProjectSummaryResponse(
                p.getId(),
                p.getGroupId(),
                p.getPart(),
                p.getTitle(),
                p.getSummary(),
                p.getThumbnailUrl(),
                p.isPublished(),
                p.getCreatedAt(),
                p.getUpdatedAt());
    }
}