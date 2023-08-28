package com.fundy.FundyBE.domain.user.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenInfoResponse {
    @Schema(description = "Auth 인증 유형", example = "Bearer")
    private final String grantType = "Bearer";
    @Schema(description = "액세스 jwt", example = "adfkjakldfjklavjklajdkla")
    private String accessToken;
    @Schema(description = "리프레쉬 jwt", example = "adfkjakldfjklavjklajdkla")
    private String refreshToken;
    @Builder
    private TokenInfoResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
