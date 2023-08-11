package com.fundy.FundyBE.global.config.security.filter;

import com.fundy.FundyBE.global.component.jwt.JwtProvider;
import com.fundy.FundyBE.global.component.jwt.TokenType;
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

    public static JwtAuthenticationFilter newInstance(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("JwtAuthenticationFilter work");
        String token = jwtProvider.resolveToken((HttpServletRequest) request);
        if(token != null && jwtProvider.isVerifyToken(token, TokenType.ACCESS)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}
