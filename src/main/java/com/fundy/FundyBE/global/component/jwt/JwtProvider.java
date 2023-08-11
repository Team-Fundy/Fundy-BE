package com.fundy.FundyBE.global.component.jwt;

import com.fundy.FundyBE.global.exception.customException.NoAuthorityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {
    private final Key key;
    private final String AUTH_CLAIM_NAME = "auth";
    private final String CODE_CLAIM_NAME = "code";

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateEmailVerifyToken(String email, String verifyCode) {
        Date now = new Date();
        final long tokenValidTime = 3 * 60 * 1000L; // 3분
        return Jwts.builder()
                .setSubject(email)
                .claim(CODE_CLAIM_NAME, verifyCode)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isVerifyEmailTokenWithCode(String token, String email, String code) {
        Claims claims = parseClaims(token);
        String tokenEmail = claims.getSubject();
        String tokenCode = claims.get(CODE_CLAIM_NAME).toString();

        if(email.equals(tokenEmail) && code.equals(tokenCode)) {
            return true;
        }

        return false;
    }

    public TokenInfo generateToken(Authentication authentication) {
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date now = new Date();
        final long tokenValidTime = 30 * 60 * 1000L; // 30분 초 밀리세컨드
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTH_CLAIM_NAME, authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        final long refreshTokenValidTime = 24 * 60 * 60 * 1000L; // 24시간 분 초 밀리세컨드
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if(claims.get(AUTH_CLAIM_NAME) == null) {
            NoAuthorityException.createBasic();
        }

        List<GrantedAuthority> authorities = ((List<String>) claims.get("auth")).stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UserDetails principal = new User(
                claims.getSubject(),
                "",
                authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean isVerifyToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // Expired된 Jwt 던짐
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}