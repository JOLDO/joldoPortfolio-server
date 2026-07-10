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
}