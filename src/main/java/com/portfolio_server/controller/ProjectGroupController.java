package com.portfolio_server.controller;

import com.portfolio_server.dto.ProjectGroupDetailResponse;
import com.portfolio_server.dto.ProjectGroupRequest;
import com.portfolio_server.dto.ProjectGroupSummaryResponse;
import com.portfolio_server.entity.ProjectGroup;
import com.portfolio_server.service.ProjectGroupService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * /api/team-groups, /api/personal-groups, /api/company-groups 를 한 벌의 코드로 처리한다.
 * 글(/api/{category}-projects)과 같은 방식이고, 이쪽은 그 글들을 묶는 단계다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectGroupController {

    private static final String BASE = "/{category:team|personal|company}-groups";

    private final ProjectGroupService service;

    /** Public: 카테고리 화면의 가로 스크롤 카드 목록. 관리자면 비공개 그룹도 포함. */
    @GetMapping(BASE)
    public List<ProjectGroupSummaryResponse> list(@PathVariable String category, HttpServletRequest request) {
        return service.findAll(category, request.isUserInRole("ADMIN")).stream()
                .map(ProjectGroupSummaryResponse::from)
                .toList();
    }

    /** Public: 모달용 상세 — 그룹 정보 + 그 그룹에 속한 글(파트) 목록. */
    @GetMapping(BASE + "/{id}")
    public ProjectGroupDetailResponse detail(
            @PathVariable String category, @PathVariable Long id, HttpServletRequest request) {
        boolean isAdmin = request.isUserInRole("ADMIN");
        ProjectGroup group = service.findById(category, id, isAdmin);
        return ProjectGroupDetailResponse.from(group, service.findPosts(category, id, isAdmin));
    }

    /** Admin: create. */
    @PostMapping(BASE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectGroupSummaryResponse create(
            @PathVariable String category, @RequestBody ProjectGroupRequest request) {
        return ProjectGroupSummaryResponse.from(service.create(category, request));
    }

    /** Admin: update. */
    @PutMapping(BASE + "/{id}")
    public ProjectGroupSummaryResponse update(
            @PathVariable String category, @PathVariable Long id, @RequestBody ProjectGroupRequest request) {
        return ProjectGroupSummaryResponse.from(service.update(category, id, request));
    }

    /** Admin: delete. 그룹만 지우고 그 안의 글은 묶음만 풀린다. */
    @DeleteMapping(BASE + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String category, @PathVariable Long id) {
        service.delete(category, id);
    }
}
