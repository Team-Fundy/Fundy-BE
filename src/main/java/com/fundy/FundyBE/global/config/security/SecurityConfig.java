package com.fundy.FundyBE.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundy.FundyBE.global.component.jwt.JwtProvider;
import com.fundy.FundyBE.global.config.security.filter.JwtAuthenticationFilter;
import com.fundy.FundyBE.global.exception.response.ExceptionResponse;
import com.fundy.FundyBE.global.exception.response.JwtExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        JwtAuthenticationFilter.newInstance(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) -> {
                    auth
                            .requestMatchers(
                                    "/user/info").authenticated()
                            .anyRequest().permitAll();
                })
                .exceptionHandling((exceptionHanndler) -> {
                    exceptionHanndler.authenticationEntryPoint(getAuthenticationEntryPoint());
                });

        return http.build();
    }

    private AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json;charset=UTF-8"); // MediaType.APPLICATION_JSON => 인코딩 문제 존재

            // 이메일 로그인 문제
            if(request.getRequestURI().equals("/api/user/login")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(objectMapper.writeValueAsString(ExceptionResponse.builder()
                        .message("로그인에 실패하였습니다")
                        .build()));
                return;
            }

            // 토큰 문제
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(objectMapper.writeValueAsString(getJwtExceptionResponse(request)));
        };
    }

    private JwtExceptionResponse getJwtExceptionResponse(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if(token!=null && jwtProvider.canRefresh(token)) {
            return JwtExceptionResponse.builder()
                    .success(false)
                    .message("토큰 만료")
                    .canRefresh(true)
                    .build();
        }

        return JwtExceptionResponse.builder()
                .success(false)
                .message("토큰값 이상")
                .canRefresh(false)
                .build();
    }



    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
