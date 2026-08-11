package com.portfolio_server.repository;

import com.portfolio_server.entity.ProjectGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long> {

    // 먼저 등록한 것이 앞(왼쪽)에 오도록 오래된 순으로 정렬한다.
    // 그래야 캐러셀에서 1차 → 2차 → 3차 → 4차 순으로 보인다.

    // 관리자용: 비공개 포함 전체
    List<ProjectGroup> findAllByCategoryOrderByCreatedAtAsc(String category);

    Optional<ProjectGroup> findByIdAndCategory(Long id, String category);

    boolean existsByIdAndCategory(Long id, String category);

    // 방문자용: 공개(published=true)된 것만
    List<ProjectGroup> findAllByCategoryAndPublishedTrueOrderByCreatedAtAsc(String category);

    Optional<ProjectGroup> findByIdAndCategoryAndPublishedTrue(Long id, String category);
}
