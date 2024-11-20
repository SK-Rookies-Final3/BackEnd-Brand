package com.shoppingoo.brand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CORS 설정
        http.cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOriginPattern("http://localhost:*");
                    config.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTIONS", "PATCH")); // 허용할 HTTP 메소드
                    config.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
                    config.setAllowCredentials(true); // 쿠키와 인증 정보를 허용
                    return config;
                }))
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authz -> authz
                        // Swagger UI와 API Docs 경로에 대한 예외 처리
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger UI 및 API Docs 허용
                        .requestMatchers("/update").authenticated() // /update 경로는 인증된 사용자만 접근 가능
                        .anyRequest().permitAll() // 그 외 경로는 모두 허용
                );

        return http.build();
    }
}
