package com.portfolio_server.dto;

import com.portfolio_server.entity.Project;
import com.portfolio_server.entity.ProjectGroup;
import java.time.Instant;
import java.util.List;

/**
 * 모달용 상세 뷰. 그룹 정보 + 그 그룹에 속한 글 목록(파트별 버튼용)을 함께 내려준다.
 * 글 본문(content)은 무거우니 여기서는 빼고, 상세 화면에서 따로 받아간다.
 */
public record ProjectGroupDetailResponse(
        Long id,
        String name,
        String summary,
        String description,
        String contribution,
        String period,
        String thumbnailUrl,
        boolean published,
        Instant createdAt,
        Instant updatedAt,
        List<Post> posts) {

    /** 그룹에 속한 글 하나. part = 웹/앱/서버 같은 담당 파트. */
    public record Post(Long id, String part, String title, String summary, boolean published) {

        public static Post from(Project p) {
            return new Post(p.getId(), p.getPart(), p.getTitle(), p.getSummary(), p.isPublished());
        }
    }

    public static ProjectGroupDetailResponse from(ProjectGroup g, List<Project> posts) {
        return new ProjectGroupDetailResponse(
                g.getId(),
                g.getName(),
                g.getSummary(),
                g.getDescription(),
                g.getContribution(),
                g.getPeriod(),
                g.getThumbnailUrl(),
                g.isPublished(),
                g.getCreatedAt(),
                g.getUpdatedAt(),
                posts.stream().map(Post::from).toList());
    }
}
