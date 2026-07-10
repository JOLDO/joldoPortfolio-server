package com.portfolio_server.service;

import com.portfolio_server.dto.ProjectRequest;
import com.portfolio_server.entity.Project;
import com.portfolio_server.repository.ProjectRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final Set<String> ALLOWED_CATEGORIES = Set.of("team", "personal", "company");    //허용되는 카테고리 종류

    private final ProjectRepository repository;

    // includePrivate=true(관리자/로그인)면 비공개 포함 전체, false(방문자)면 공개된 것만 조회한다.
    @Transactional(readOnly = true)
    public List<Project> findAll(String category, boolean includePrivate) {
        checkCategory(category);
        return includePrivate
                ? repository.findAllByCategoryOrderByCreatedAtDesc(category)
                : repository.findAllByCategoryAndPublishedTrueOrderByCreatedAtDesc(category);
    }

    @Transactional(readOnly = true)
    public Project findById(String category, Long id, boolean includePrivate) {
        checkCategory(category);
        //레포지토리에 없어도 기본적으로 사용 할수 있음 save나 deletebyid도
        return (includePrivate
                ? repository.findByIdAndCategory(id, category)
                : repository.findByIdAndCategoryAndPublishedTrue(id, category))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found: " + category + "/" + id));
    }

    @Transactional
    public Project create(String category, ProjectRequest request) {
        checkCategory(category);
        Project project = new Project();
        project.setCategory(category);  //category는 body가 아니라 URL에서 받아 저장
        apply(project, request);
        return repository.save(project);    //save는 upsert로 작동됨
    }

    @Transactional
    public Project update(String category, Long id, ProjectRequest request) {
        Project project = findById(category, id, true);   //관리자만 호출(비공개 글도 수정해야 하므로 includePrivate=true), 존재+category 일치 확인 겸함
        apply(project, request);
        return repository.save(project);
    }

    @Transactional
    public void delete(String category, Long id) {
        checkCategory(category);
        if (!repository.existsByIdAndCategory(id, category)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found: " + category + "/" + id);
        }
        repository.deleteById(id);
    }

    private void apply(Project project, ProjectRequest request) {
        project.setTitle(request.title());
        project.setSummary(request.summary());
        project.setContent(request.content());
        project.setThumbnailUrl(request.thumbnailUrl());
        project.setPublished(request.published() == null || request.published());   //값이 없으면(null) 공개(true)로 처리
    }

    /** team/personal/company 외의 값이면 404로 막는다. */
    private void checkCategory(String category) {
        if (!ALLOWED_CATEGORIES.contains(category)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown category: " + category);
        }
    }
}