package com.portfolio_server.service;

import com.portfolio_server.dto.ProjectGroupRequest;
import com.portfolio_server.entity.Project;
import com.portfolio_server.entity.ProjectGroup;
import com.portfolio_server.repository.ProjectGroupRepository;
import com.portfolio_server.repository.ProjectRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * 프로젝트 그룹(팀의 "1차", 회사의 "미플") CRUD.
 * 글(Project)을 다루는 ProjectService와 규칙을 똑같이 맞췄다 —
 * 카테고리 검증, 비공개는 관리자에게만.
 */
@Service
@RequiredArgsConstructor
public class ProjectGroupService {

    private static final Set<String> ALLOWED_CATEGORIES = Set.of("team", "personal", "company");

    private final ProjectGroupRepository repository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectGroup> findAll(String category, boolean includePrivate) {
        checkCategory(category);
        return includePrivate
                ? repository.findAllByCategoryOrderByCreatedAtAsc(category)
                : repository.findAllByCategoryAndPublishedTrueOrderByCreatedAtAsc(category);
    }

    @Transactional(readOnly = true)
    public ProjectGroup findById(String category, Long id, boolean includePrivate) {
        checkCategory(category);
        return (includePrivate
                ? repository.findByIdAndCategory(id, category)
                : repository.findByIdAndCategoryAndPublishedTrue(id, category))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ProjectGroup not found: " + category + "/" + id));
    }

    /** 모달에 파트 버튼으로 뿌릴, 이 그룹에 속한 글 목록. */
    @Transactional(readOnly = true)
    public List<Project> findPosts(String category, Long groupId, boolean includePrivate) {
        return includePrivate
                ? projectRepository.findAllByCategoryAndGroupIdOrderByCreatedAtAsc(category, groupId)
                : projectRepository.findAllByCategoryAndGroupIdAndPublishedTrueOrderByCreatedAtAsc(category, groupId);
    }

    @Transactional
    public ProjectGroup create(String category, ProjectGroupRequest request) {
        checkCategory(category);
        ProjectGroup group = new ProjectGroup();
        group.setCategory(category);    //category는 body가 아니라 URL에서 받아 저장
        apply(group, request);
        return repository.save(group);
    }

    @Transactional
    public ProjectGroup update(String category, Long id, ProjectGroupRequest request) {
        ProjectGroup group = findById(category, id, true);   //관리자만 호출하므로 비공개도 찾을 수 있게 true
        apply(group, request);
        return repository.save(group);
    }

    /**
     * 그룹을 지운다. 이때 그 그룹에 묶여 있던 글은 지우지 않고 groupId만 비운다.
     * (글은 그대로 두고 묶음만 푸는 것 — 실수로 그룹을 지웠을 때 글까지 날아가면 곤란하니까)
     */
    @Transactional
    public void delete(String category, Long id) {
        checkCategory(category);
        if (!repository.existsByIdAndCategory(id, category)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectGroup not found: " + category + "/" + id);
        }
        for (Project post : projectRepository.findAllByGroupId(id)) {
            post.setGroupId(null);
            post.setPart(null);
            projectRepository.save(post);
        }
        repository.deleteById(id);
    }

    private void apply(ProjectGroup group, ProjectGroupRequest request) {
        group.setName(request.name());
        group.setSummary(request.summary());
        group.setDescription(request.description());
        group.setContribution(request.contribution());
        group.setPeriod(request.period());
        group.setThumbnailUrl(request.thumbnailUrl());
        group.setPublished(request.published() == null || request.published());     //값이 없으면(null) 공개(true)
    }

    private void checkCategory(String category) {
        if (!ALLOWED_CATEGORIES.contains(category)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown category: " + category);
        }
    }
}
