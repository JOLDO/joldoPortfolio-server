package com.portfolio_server.config;

import com.portfolio_server.security.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.cors.allowed-origins:http://localhost:3000}")
    private String[] allowedOrigins;    //cors 허용 도매인

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }   //맴버 비밀번호 해싱에 사용

    /**
     * Static resources (uploaded images) bypass the security filter chain entirely —
     * no JWT filter, no auth checks — since they are public files served by the
     * ResourceHandler in WebConfig.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/uploads/**");    //정적파일 security 무시
    }

    /** Uses the CustomUserDetailsService + PasswordEncoder beans auto-wired by Spring Boot. */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();    //로그인 시 맴버 id/pw 확인 매니저 사용위해 가져옴
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   //jwt사용시 csrf는 필요 없음 이유: 쿠키/세션 기반은 브라우저가 자동으로 붙여서 보내줘서 요청이 가능한데 jwt는 헤더에 실어서 보내서 의미 없음
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //상태를 사용하지 않고 jwt는 토큰만 보내므로 stateless로 하면됨
                .authorizeHttpRequests(auth -> auth //허용 경로 지정
                        // Let internal error dispatches through so real status codes (401/404/...) survive
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()   //에러로 인한 재요청이 필터에 걸려 마지막인 anyRequest().hasRole("ADMIN"))로 가서 에러가 권한 에러처럼 떠버리는데 허용해서 통과 되도록 해서 실제 에러로 나오게 함
                        // Public: login, swagger (static /uploads is ignored via WebSecurityCustomizer)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // Public: all read (GET) endpoints
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll() //get요청의 /api/**는 모두 허용
                        // Everything else (writes) requires an ADMIN
                        .anyRequest().hasRole("ADMIN")) //나머지는 ADMIN역할일때만 통과, 이건 자동적으로 ROLE_ADMIN으로 검사함
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  //jwtAuthenticationFilter필터를 필터체인에 등록
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));    //허용 도매인
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));   //해당 메서드 허용   //preflight라고 하는 options로 먼저 한번 가능할지 날려보는 부분을 허용
        config.setAllowedHeaders(List.of("*")); //헤더의 설정 모두 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);    //모든 경로에 cors 설정을 적용
        return source;
    }
}