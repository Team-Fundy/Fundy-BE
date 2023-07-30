package com.fundy.FundyBE.domain.user.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Schema(description = "인증코드 관련 이메일과 토큰")
public class EmailCodeResponse {
    @Schema(description = "유저가 인증할 이메일", example = "dongwon0103@naver.com")
    private String email;
    @Schema(description = "인증 관련 토큰", example = "adfjkladfjkladfjakl")
    private String token;
}
