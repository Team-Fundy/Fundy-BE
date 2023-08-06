package com.fundy.FundyBE.global.component.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenInfo {
    @Schema(description = "Auth 인증 유형", example = "Bearer")
    private final String grantType = "Bearer";
    @Schema(description = "액세스 jwt", example = "adfkjakldfjklavjklajdkla")
    private String accessToken;
    @Schema(description = "리프레쉬 jwt", example = "adfkjakldfjklavjklajdkla")
    private String refreshToken;
    @Builder
    private TokenInfo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
