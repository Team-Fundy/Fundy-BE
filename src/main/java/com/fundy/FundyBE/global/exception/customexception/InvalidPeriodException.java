package com.fundy.FundyBE.global.exception.customexception;

public class InvalidPeriodException extends RuntimeException {
    private InvalidPeriodException(String message) {
        super(message);
    }

    public final static InvalidPeriodException createBasic() {
        return new InvalidPeriodException("유효하지 않은 기간 설정입니다");
    }
}
