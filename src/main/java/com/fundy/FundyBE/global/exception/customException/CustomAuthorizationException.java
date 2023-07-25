package com.fundy.FundyBE.global.exception.customException;

public class CustomAuthorizationException extends RuntimeException{
    private CustomAuthorizationException(String message) {
        super(message);
    }

    public static CustomAuthorizationException createBasic() {
        return new CustomAuthorizationException("인증에 문제가 생겼습니다.");
    }

    public static CustomAuthorizationException withCustomMessage(String message) {
        return new CustomAuthorizationException(message);
    }
}
