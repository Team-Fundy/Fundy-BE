package com.fundy.FundyBE.global.jwt;

import com.fundy.FundyBE.domain.user.repository.FundyRole;
import com.fundy.FundyBE.domain.user.repository.FundyUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
    private final String testSecretKey = "adfajdfhajkdfjklafhjkefhjkhrjkdhajkfdhajkfdjf";

    JwtProvider jwtProvider = new JwtProvider(testSecretKey);

    @Mock
    Authentication mockAuthentication;

    @Test
    void generateToken() {
        String userName = "3bff";
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        doReturn(authorities).when(mockAuthentication).getAuthorities();
        doReturn(userName).when(mockAuthentication).getName();

        TokenInfo tokenInfo = jwtProvider.generateToken(mockAuthentication);
        System.out.println(tokenInfo.getGrantType());
        System.out.println(tokenInfo.getAccessToken());
        System.out.println(tokenInfo.getRefreshToken());
    }
}