package com.fundy.FundyBE.global.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenInfo {
    private final String grantType = "Bearer";
    private String accessToken;
    private String refreshToken;
    @Builder
    private TokenInfo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
