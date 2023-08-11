package com.fundy.FundyBE.global.exception.customException;

public class RefreshTokenException extends RuntimeException{
    private RefreshTokenException(String message) {
        super(message);
    }

    public final static RefreshTokenException createBasic() {
        return new RefreshTokenException("리프레쉬 토큰 발급 실패하였습니다");
    }

    public final static RefreshTokenException withCustomMessage(String message) {
        return new RefreshTokenException(message);
    }
}
