package com.fundy.FundyBE.global.exception.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {
    private boolean success = false;
    private String message;
    @Builder
    private ExceptionResponse(String message) {
        this.message = message;
    }
}
