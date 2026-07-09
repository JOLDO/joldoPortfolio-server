package com.portfolio_server.controller;

import com.portfolio_server.dto.TeamProjectDetailResponse;
import com.portfolio_server.dto.TeamProjectRequest;
import com.portfolio_server.dto.TeamProjectSummaryResponse;
import com.portfolio_server.service.TeamProjectService;
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

@RestController
@RequestMapping("/api/team-projects")
@RequiredArgsConstructor
public class TeamProjectController {

    private final TeamProjectService service;

    /** Public: list for the /teamProject page. */
    @GetMapping
    public List<TeamProjectSummaryResponse> list() {    //리스트를 읽어오는 화면은 무거운 본문 내용이 필요 없어서 일단 읽어온 후에 dto에 필요한 내용만 다시 담아준다. 그래서 타입이 바뀌므로 steam으로 하나하나 바꿔서 리스트로 받아온다.
        return service.findAll().stream()
                .map(TeamProjectSummaryResponse::from)
                .toList();
    }

    /** Public: detail for /teamProject/{id}. */
    @GetMapping("/{id}")    //요청 경로에 값을 넣어주면 @PathVariable로 받아진다.
    public TeamProjectDetailResponse detail(@PathVariable Long id) {
        return TeamProjectDetailResponse.from(service.findById(id));
    }

    /** Admin: create. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //응답 성공시 해당 상태를 보내준다. CREATED는 201
    public TeamProjectDetailResponse create(@RequestBody TeamProjectRequest request) {
        return TeamProjectDetailResponse.from(service.create(request));
    }

    /** Admin: update. */
    @PutMapping("/{id}")
    public TeamProjectDetailResponse update(@PathVariable Long id, @RequestBody TeamProjectRequest request) {
        return TeamProjectDetailResponse.from(service.update(id, request));
    }

    /** Admin: delete. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  //응답 성공시 해당 상태를 보내준다. NO_CONTENT 204
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}