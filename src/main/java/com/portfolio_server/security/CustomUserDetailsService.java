package com.portfolio_server.security;

import com.portfolio_server.entity.Member;
import com.portfolio_server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Loads users from the DB for Spring Security (used by both login and the JWT filter). */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)   //Member테이블에서 유저 이름으로 데이터를 읽어옴
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + member.getRole()))    //역할은 지정한 역할의 앞에 ROLE_을 붙여서 검색하기 때문
                .build();   //UserDetails형식으로 반환
    }
}