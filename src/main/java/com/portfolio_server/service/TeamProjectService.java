package com.portfolio_server.service;

import com.portfolio_server.dto.TeamProjectRequest;
import com.portfolio_server.entity.TeamProject;
import com.portfolio_server.repository.TeamProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TeamProjectService {

    private final TeamProjectRepository repository;

    @Transactional(readOnly = true)
    public List<TeamProject> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public TeamProject findById(Long id) {
        return repository.findById(id)  //레포지토리에 없어도 기본적으로 사용 할수 있음 save나 deletebyid도
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team project not found: " + id));
    }

    @Transactional
    public TeamProject create(TeamProjectRequest request) {
        TeamProject project = new TeamProject();
        apply(project, request);
        return repository.save(project);    //save는 upsert로 작동됨
    }

    @Transactional
    public TeamProject update(Long id, TeamProjectRequest request) {
        TeamProject project = findById(id);
        apply(project, request);
        return repository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team project not found: " + id);
        }
        repository.deleteById(id);
    }

    private void apply(TeamProject project, TeamProjectRequest request) {
        project.setTitle(request.title());
        project.setSummary(request.summary());
        project.setContent(request.content());
        project.setThumbnailUrl(request.thumbnailUrl());
    }
}