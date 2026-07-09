package com.portfolio_server.repository;

import com.portfolio_server.entity.TeamProject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamProjectRepository extends JpaRepository<TeamProject, Long> {

    List<TeamProject> findAllByOrderByCreatedAtDesc();
}