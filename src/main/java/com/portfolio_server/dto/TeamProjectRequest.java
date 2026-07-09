package com.portfolio_server.dto;

/** Payload for creating/updating a team project (admin only). */
public record TeamProjectRequest(
        String title,
        String summary,
        String content,
        String thumbnailUrl) {
}