package com.fundy.FundyBE.global.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {
    @Schema(description = "API 호출 시 처리 성공 여부", example = "false")
    private boolean success = false;
    @Schema(description = "에러 발생 시 메시지", example = "에러 이유")
    private String message;
    @Builder
    private ExceptionResponse(String message) {
        this.message = message;
    }
}
