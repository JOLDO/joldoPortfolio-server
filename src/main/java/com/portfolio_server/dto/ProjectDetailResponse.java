package com.portfolio_server.dto;

import com.portfolio_server.entity.Project;
import java.time.Instant;

/** Full view for the detail page, including the editor content body. */
public record ProjectDetailResponse(
        Long id,
        String title,
        String summary,
        String content,
        String thumbnailUrl,
        boolean published,
        Instant createdAt,
        Instant updatedAt) {

    public static ProjectDetailResponse from(Project p) {
        return new ProjectDetailResponse(
                p.getId(),
                p.getTitle(),
                p.getSummary(),
                p.getContent(),
                p.getThumbnailUrl(),
                p.isPublished(),
                p.getCreatedAt(),
                p.getUpdatedAt());
    }
}