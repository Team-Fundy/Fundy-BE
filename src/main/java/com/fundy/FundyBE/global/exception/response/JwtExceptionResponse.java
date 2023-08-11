package com.fundy.FundyBE.global.exception.response;

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
@Schema(name = "토큰 에러", description = "토큰 문제 시 발생")
public class JwtExceptionResponse {
    @Schema(description = "API 호출 성공 여부", example = "false")
    boolean success;
    @Schema(description = "토큰 에러 메시지", example = "토큰 만료")
    String message;
    @Schema(description = "토큰 재발급 가능", example = "true")
    boolean canRefresh;
}
