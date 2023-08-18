package com.fundy.FundyBE.global.exception.customException;

public class AuthTypeMismatchException extends RuntimeException{
    private AuthTypeMismatchException(String message) {
        super(message);
    }

    public static AuthTypeMismatchException createBasic() {
        return new AuthTypeMismatchException("간편 로그인 이력이 있습니다");
    }
}
