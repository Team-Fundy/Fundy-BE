package com.fundy.FundyBE.global.security.filter;

import com.fundy.FundyBE.global.exception.customException.CustomAuthorizationException;
import com.fundy.FundyBE.global.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

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
        String token = resolveToken((HttpServletRequest) request);
        if(token != null && jwtProvider.isVerifyToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
//        throw CustomAuthorizationException.createBasic();
    }
}