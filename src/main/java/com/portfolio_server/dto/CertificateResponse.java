package com.portfolio_server.dto;

import com.portfolio_server.entity.Certificate;

public record CertificateResponse(Long id, String name, String issuer, String acquiredDate) {

    public static CertificateResponse from(Certificate c) {
        return new CertificateResponse(c.getId(), c.getName(), c.getIssuer(), c.getAcquiredDate());
    }
}
