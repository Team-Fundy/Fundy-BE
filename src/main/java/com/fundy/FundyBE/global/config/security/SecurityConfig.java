package com.fundy.FundyBE.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundy.FundyBE.domain.user.repository.FundyRole;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.global.component.jwt.JwtProvider;
import com.fundy.FundyBE.global.component.jwt.JwtUtil;
import com.fundy.FundyBE.global.component.jwt.dto.response.TokenInfo;
import com.fundy.FundyBE.global.config.redis.logoutInfo.LogoutInfoRedisRepository;
import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshInfo;
import com.fundy.FundyBE.global.config.redis.refreshInfo.RefreshInfoRedisRepository;
import com.fundy.FundyBE.global.config.security.filter.JwtAuthenticationFilter;
import com.fundy.FundyBE.global.config.security.oauth2.CustomOauth2UserService;
import com.fundy.FundyBE.global.config.security.oauth2.exception.AuthTypeMismatchOAuth2Exception;
import com.fundy.FundyBE.global.config.security.userDetail.CustomUserDetails;
import com.fundy.FundyBE.global.exception.response.ExceptionResponse;
import com.fundy.FundyBE.global.exception.response.JwtExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final CustomOauth2UserService customOauth2UserService;
    private final RefreshInfoRedisRepository refreshInfoRedisRepository;
    private final UserRepository userRepository;
    private final LogoutInfoRedisRepository logoutInfoRedisRepository;
    // FIXME: 서비스할 때 고쳐야함;
    private final String OAUTH2_CLIENT_PATH = "http://localhost:3000/oauth2";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        JwtAuthenticationFilter.newInstance(jwtProvider, logoutInfoRedisRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) -> {
                    auth
                            .requestMatchers(
                                    "/user/info",
                                    "/user/logout").authenticated()
                            .requestMatchers("/user/creator").hasAuthority(FundyRole.NORMAL_USER.getValue())
                            .anyRequest().permitAll();
                })
                .exceptionHandling((exceptionHandler) -> {
                    exceptionHandler.authenticationEntryPoint(getAuthenticationEntryPoint());
                })
                .oauth2Login(oauth2LoginConfig -> {
                    oauth2LoginConfig
                            .authorizationEndpoint(authorizationEndpointConfig -> {
                                authorizationEndpointConfig.baseUri("/user/oauth2/login");
                            })
                            .redirectionEndpoint(redirectionEndpointConfig -> {
                                redirectionEndpointConfig.baseUri("/user/oauth2/code/*");
                            })
                            .userInfoEndpoint(userInfoEndpointConfig -> {
                                userInfoEndpointConfig.userService(customOauth2UserService);
                            })
                            .successHandler(getAuthenticationSuccessHandler())
                            .failureHandler(getAuthenticationFailureHandler());
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

            if(request.getRequestURI().equals("/api/user/logout")) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write(objectMapper.writeValueAsString(ExceptionResponse.builder()
                        .message("로그아웃에 실패하였습니다")
                        .build()));
                return;
            }

            // 토큰 문제
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(objectMapper.writeValueAsString(getJwtExceptionResponse(request)));
        };
    }

    private JwtExceptionResponse getJwtExceptionResponse(HttpServletRequest request) {
        String token = JwtUtil.resolveToken(request);
        if (token!=null && jwtProvider.canRefresh(token)) {
            if (logoutInfoRedisRepository.findByAccessToken(token).isPresent()) {
                return JwtExceptionResponse.builder()
                        .success(false)
                        .message("로그아웃 상태")
                        .canRefresh(false)
                        .build();
            }

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

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if(response.isCommitted()) {
                log.debug("Oauth2 Success response Already Commited");
                return;
            }

            UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            List<String> authorities = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if(authorities.size() != 1) {
                throw new RuntimeException("User have only 1 Authority");
            }

            FundyRole role = FundyRole.valueOf(authorities.get(0));
            if(role.equals(FundyRole.GUEST)) {
                processOAuth2GuestUser(request, response, authentication);
                return;
            }
            processOAuth2NormalUser(request,response,authentication);
        };
    }

    private void processOAuth2GuestUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        FundyUser user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("Can't find OAuth2 User"));

        user.setRole(FundyRole.NORMAL_USER);
        userRepository.save(user);

        TokenInfo tokenInfo = jwtProvider.generateToken(userDetails.getUsername(), FundyRole.NORMAL_USER);
        refreshInfoRedisRepository.save(RefreshInfo.builder()
                        .id(userDetails.getUsername())
                        .authorities(Stream.of(FundyRole.NORMAL_USER.getValue())
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()))
                        .refreshToken(tokenInfo.getRefreshToken())
                .build());

        String targetUrl = UriComponentsBuilder.fromUriString(OAUTH2_CLIENT_PATH+"/success")
                .queryParam("grant", tokenInfo.getGrantType())
                .queryParam("access", tokenInfo.getAccessToken())
                .queryParam("refresh", tokenInfo.getRefreshToken())
                .queryParam("first", true)
                .toUriString();

        redirect(request, response, targetUrl);
    }

    private void processOAuth2NormalUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        refreshInfoRedisRepository.save(RefreshInfo.builder()
                        .id(userDetails.getUsername())
                        .authorities(userDetails.getAuthorities())
                        .refreshToken(tokenInfo.getRefreshToken())
                .build());

        String targetUrl = UriComponentsBuilder.fromUriString(OAUTH2_CLIENT_PATH+"/success")
                .queryParam("grant", tokenInfo.getGrantType())
                .queryParam("access", tokenInfo.getAccessToken())
                .queryParam("refresh", tokenInfo.getRefreshToken())
                .queryParam("first", false)
                .toUriString();

        redirect(request,response, targetUrl);
    }

    private AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            String targetUrl;
            if(exception instanceof AuthTypeMismatchOAuth2Exception) {
                targetUrl = UriComponentsBuilder.fromUriString(OAUTH2_CLIENT_PATH + "/fail/email")
                        .toUriString();
            } else {
                targetUrl = UriComponentsBuilder.fromUriString(OAUTH2_CLIENT_PATH + "/fail/exception")
                        .toUriString();
            }
            redirect(request, response, targetUrl);
        };
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String targetUrl) throws IOException {
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
