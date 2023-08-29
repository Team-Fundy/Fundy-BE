package com.fundy.FundyBE.global.component.jwt;

import com.fundy.FundyBE.global.constraint.FundyRole;
import com.fundy.FundyBE.global.component.jwt.dto.response.TokenInfo;
import com.fundy.FundyBE.global.config.redis.refreshinfo.RefreshInfo;
import com.fundy.FundyBE.global.exception.customexception.NoAuthorityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class JwtProvider {
    private final String AUTH_CLAIM_NAME = "auth";
    private final Key accessKey;
    private final Key refreshKey;

    public JwtProvider(@Value("${jwt.secret.access}") String secretAccessKey,
                       @Value("${jwt.secret.refresh}") String secretRefreshKey) {
         this.accessKey = JwtUtil.parseKey(secretAccessKey);
         this.refreshKey = JwtUtil.parseKey(secretRefreshKey);
    }

    public TokenInfo generateToken(Authentication authentication) {
        return buildTokenInfo(
                parseAuthorities(authentication.getAuthorities()),
                authentication.getName());
    }

    public TokenInfo generateToken(String email, FundyRole role) {
        return buildTokenInfo(
                Stream.of(role.getValue()).toList(),
                email);
    }

    public TokenInfo generateTokenWithRefreshInfo(RefreshInfo refreshInfo) {
        return buildTokenInfo(
                parseAuthorities(refreshInfo.getAuthorities()),
                refreshInfo.getId());
    }

    private List<String> parseAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private TokenInfo buildTokenInfo(List<String> authorities, String subject) {
        final long ACCESS_DURATION = 2 * 60 * 60 * 1000L; // 2시간
        final long REFRESH_DURATION = 30 * 24 * 60 * 60 * 1000L; // 30일

        Date now = new Date();
        Date AccessExpirationDate = new Date(now.getTime() + ACCESS_DURATION);
        Date RefreshExpirationDate = new Date(now.getTime() + REFRESH_DURATION);

        String accessToken = Jwts.builder()
                .setSubject(subject)
                .claim(AUTH_CLAIM_NAME, authorities)
                .setIssuedAt(now)
                .setExpiration(AccessExpirationDate)
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(RefreshExpirationDate)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = JwtUtil.parseClaims(accessToken, accessKey);

        if(claims.get(AUTH_CLAIM_NAME) == null) {
            throw NoAuthorityException.createBasic();
        }

        List<GrantedAuthority> authorities = ((List<String>) claims.get(AUTH_CLAIM_NAME)).stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UserDetails principal = new User(
                claims.getSubject(),
                "",
                authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean isVerifyAccessToken(String accessToken) {
        return JwtUtil.isVerifyToken(accessToken, accessKey);
    }

    public boolean isVerifyRefreshToken(String refreshToken) {
        return JwtUtil.isVerifyToken(refreshToken, refreshKey);
    }

    public boolean canRefresh(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
