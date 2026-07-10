package com.portfolio_server.controller;

import com.portfolio_server.dto.ProjectDetailResponse;
import com.portfolio_server.dto.ProjectRequest;
import com.portfolio_server.dto.ProjectSummaryResponse;
import com.portfolio_server.service.ProjectService;
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
 * Handles /api/team-projects, /api/personal-projects, /api/company-projects with one set of code.
 * The {category} path variable is constrained by regex so only these three prefixes match;
 * anything else (e.g. /api/foo-projects) simply 404s.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private static final String BASE = "/{category:team|personal|company}-projects";    //category경로 변수가 team, personal, company 3중 하나를 가지며 -project를 뒤에 붙인다.

    private final ProjectService service;

    /** Public: list for the category's page. 로그인(관리자)이면 비공개 글도 포함해서 보여준다. */
    @GetMapping(BASE)
    public List<ProjectSummaryResponse> list(@PathVariable String category, HttpServletRequest request) {   //리스트를 읽어오는 화면은 무거운 본문 내용이 필요 없어서 일단 읽어온 후에 dto에 필요한 내용만 다시 담아준다. 그래서 타입이 바뀌므로 stream으로 하나하나 바꿔서 리스트로 받아온다.
        return service.findAll(category, request.isUserInRole("ADMIN")).stream()    //카테고리별, 관리자인지
                .map(ProjectSummaryResponse::from)
                .toList();
    }

    /** Public: detail for /{category}-projects/{id}. 비공개 글은 관리자에게만 보인다(방문자는 404). */
    @GetMapping(BASE + "/{id}")    //요청 경로에 값을 넣어주면 @PathVariable로 받아진다.
    public ProjectDetailResponse detail(@PathVariable String category, @PathVariable Long id, HttpServletRequest request) {
        return ProjectDetailResponse.from(service.findById(category, id, request.isUserInRole("ADMIN")));
    }

    /** Admin: create. */
    @PostMapping(BASE)
    @ResponseStatus(HttpStatus.CREATED) //응답 성공시 해당 상태를 보내준다. CREATED는 201
    public ProjectDetailResponse create(@PathVariable String category, @RequestBody ProjectRequest request) {
        //카테고리를 경로에서 가져오고 나머지는 바디에서 읽어온다. 나눠서 하는 이유는 get이랑 delete는 카테고리랑 id만 알면 되기 때문에 따로 받아오는게 맞음
        return ProjectDetailResponse.from(service.create(category, request));
    }

    /** Admin: update. */
    @PutMapping(BASE + "/{id}")
    public ProjectDetailResponse update(
            @PathVariable String category, @PathVariable Long id, @RequestBody ProjectRequest request) {
        return ProjectDetailResponse.from(service.update(category, id, request));
    }

    /** Admin: delete. */
    @DeleteMapping(BASE + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  //응답 성공시 해당 상태를 보내준다. NO_CONTENT 204
    public void delete(@PathVariable String category, @PathVariable Long id) {
        service.delete(category, id);
    }
}