package com.portfolio_server.controller;

import com.portfolio_server.dto.CertificateRequest;
import com.portfolio_server.dto.CertificateResponse;
import com.portfolio_server.service.CertificateService;
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

/** /api/certificates — 조회는 누구나, 등록·수정·삭제는 ADMIN (SecurityConfig에서 일괄 적용). */
@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService service;

    @GetMapping
    public List<CertificateResponse> list() {
        return service.findAll().stream().map(CertificateResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateResponse create(@RequestBody CertificateRequest request) {
        return CertificateResponse.from(service.create(request));
    }

    @PutMapping("/{id}")
    public CertificateResponse update(@PathVariable Long id, @RequestBody CertificateRequest request) {
        return CertificateResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
