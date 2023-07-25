package com.fundy.FundyBE.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GlobalResponse<T> {
    private boolean success;
    private String message;
    private T result;
}
