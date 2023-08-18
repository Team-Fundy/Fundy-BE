package com.fundy.FundyBE.global.config.security.filter;

import com.fundy.FundyBE.global.component.jwt.JwtProvider;
import com.fundy.FundyBE.global.component.jwt.TokenType;
import com.fundy.FundyBE.global.config.redis.logoutInfo.LogoutInfoRedisRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtAuthenticationFilter extends GenericFilter {
    private final JwtProvider jwtProvider;
    private final LogoutInfoRedisRepository logoutInfoRedisRepository;

    public static JwtAuthenticationFilter newInstance(JwtProvider jwtProvider, LogoutInfoRedisRepository logoutInfoRedisRepository) {
        return new JwtAuthenticationFilter(jwtProvider, logoutInfoRedisRepository);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtProvider.resolveToken((HttpServletRequest) request);
        if(!((HttpServletRequest) request).getRequestURI().equals("/api/user/reissue")) {
            if(token != null && jwtProvider.isVerifyToken(token, TokenType.ACCESS)
            && logoutInfoRedisRepository.findByAccessToken(token).isEmpty()) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request,response);
    }
}
