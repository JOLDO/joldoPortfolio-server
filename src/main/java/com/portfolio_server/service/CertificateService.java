package com.portfolio_server.service;

import com.portfolio_server.dto.CertificateRequest;
import com.portfolio_server.entity.Certificate;
import com.portfolio_server.repository.CertificateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository repository;

    /** 자격증은 숨길 이유가 없어서 공개/비공개 구분 없이 전부 내려준다. */
    @Transactional(readOnly = true)
    public List<Certificate> findAll() {
        return repository.findAllByOrderByAcquiredDateDesc();
    }

    @Transactional
    public Certificate create(CertificateRequest request) {
        Certificate certificate = new Certificate();
        apply(certificate, request);
        return repository.save(certificate);
    }

    @Transactional
    public Certificate update(Long id, CertificateRequest request) {
        Certificate certificate = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Certificate not found: " + id));
        apply(certificate, request);
        return repository.save(certificate);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate not found: " + id);
        }
        repository.deleteById(id);
    }

    private void apply(Certificate certificate, CertificateRequest request) {
        certificate.setName(request.name());
        certificate.setIssuer(request.issuer());
        certificate.setAcquiredDate(request.acquiredDate());
    }
}
