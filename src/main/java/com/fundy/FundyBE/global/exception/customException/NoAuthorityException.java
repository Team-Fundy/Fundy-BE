package com.fundy.FundyBE.global.exception.customException;

public class NoAuthorityException extends RuntimeException{
    private NoAuthorityException(String message) {
        super(message);
    }

    public final static NoAuthorityException createBasic() {
        return new NoAuthorityException("권한 정보가 존재하지 않습니다.");
    }

    public final static NoAuthorityException withCustomMessage(String message) {
        return new NoAuthorityException(message);
    }
}
