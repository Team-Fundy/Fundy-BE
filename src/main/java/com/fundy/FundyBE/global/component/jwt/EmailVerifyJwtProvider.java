package com.fundy.FundyBE.global.component.jwt;

import com.fundy.FundyBE.global.component.jwt.dto.request.VerifyEmailInfoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class EmailVerifyJwtProvider {
    private final Key key;
    private final String CLAIM_NAME = "code";

    public EmailVerifyJwtProvider(@Value("${jwt.secret.email}") String secretKey) {
        this.key = JwtUtil.parseKey(secretKey);
    }

    public String generateToken(String email, String verifyCode) {
        Date now = new Date();
        long JWT_DURATION = 3 * 60 * 1000L; // 3분
        Date expirationDate = new Date(now.getTime() + JWT_DURATION);

        return Jwts.builder()
                .setSubject(email)
                .claim(CLAIM_NAME, verifyCode)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isVerifyToken(VerifyEmailInfoRequest verifyEmailInfoRequest) {
        Claims claims = JwtUtil.parseClaims(verifyEmailInfoRequest.getToken(), key);
        String tokenEmail = claims.getSubject();
        String tokenCode = claims.get(CLAIM_NAME).toString();

        return verifyEmailInfoRequest.getEmail().equals(tokenEmail)
                && verifyEmailInfoRequest.getCode().equals(tokenCode);
    }
}
