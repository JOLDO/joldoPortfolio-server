package com.portfolio_server.repository;

import com.portfolio_server.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 관리자용: 비공개 포함 전체
    List<Project> findAllByCategoryOrderByCreatedAtDesc(String category);

    Optional<Project> findByIdAndCategory(Long id, String category);

    boolean existsByIdAndCategory(Long id, String category);

    // 방문자용: 공개(published=true)된 것만
    List<Project> findAllByCategoryAndPublishedTrueOrderByCreatedAtDesc(String category);

    Optional<Project> findByIdAndCategoryAndPublishedTrue(Long id, String category);

    // 그룹 상세(모달)용: 그 그룹에 속한 글만. 파트 순서가 흔들리지 않게 오래된 순으로 고정한다.
    List<Project> findAllByCategoryAndGroupIdOrderByCreatedAtAsc(String category, Long groupId);

    List<Project> findAllByCategoryAndGroupIdAndPublishedTrueOrderByCreatedAtAsc(String category, Long groupId);

    // 그룹을 지울 때, 거기 묶여 있던 글들의 groupId를 비우려고 쓴다(글은 지우지 않는다).
    List<Project> findAllByGroupId(Long groupId);
}