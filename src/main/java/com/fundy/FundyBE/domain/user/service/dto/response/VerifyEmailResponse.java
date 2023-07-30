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
@Schema(name = "이메일 인증 확인", description = "이메일 인증 여부")
public class VerifyEmailResponse {
    @Schema(description = "인증한 이메일")
    String email;
    @Schema(description = "인증 여부")
    boolean verify;
}
