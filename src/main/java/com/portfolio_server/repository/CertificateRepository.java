package com.portfolio_server.repository;

import com.portfolio_server.entity.Certificate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    // 최근에 딴 자격증이 위로 오게. acquiredDate가 "2018.08" 형식이라 문자열 정렬로도 날짜순이 된다.
    List<Certificate> findAllByOrderByAcquiredDateDesc();
}
