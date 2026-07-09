package com.portfolio_server.repository;

import com.portfolio_server.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> { //<Entity, pk타입>

    Optional<Member> findByUsername(String username);   //자동적으로 이름으로 간단한 select문이 만들어짐
}