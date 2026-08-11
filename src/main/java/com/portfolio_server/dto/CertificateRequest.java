package com.portfolio_server.dto;

/** 자격증 등록/수정 payload (관리자 전용). */
public record CertificateRequest(String name, String issuer, String acquiredDate) {
}
