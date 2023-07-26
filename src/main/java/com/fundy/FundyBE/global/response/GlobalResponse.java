package com.fundy.FundyBE.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "전체 정상적인 response Wrapper")
public class GlobalResponse<T> {
    @Schema(description = "API 호출 성공 여부", example = "true")
    private boolean success = true;
    @Schema(description = "API 호출 성공 시 메시지", example = "API 정상작동 메시지")
    private String message;
    private T result;
    @Builder
    private GlobalResponse(String message, T result) {
        this.message = message;
        this.result = result;
    }
}
