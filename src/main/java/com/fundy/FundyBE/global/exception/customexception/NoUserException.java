package com.fundy.FundyBE.global.exception.customexception;

public class NoUserException extends RuntimeException{
    private NoUserException(String message) {
        super(message);
    }

    public final static NoUserException createBasic() {
        return new NoUserException("유저가 존재하지 않습니다");
    }

    public final static NoUserException withCustomMessage(String message) {
        return new NoUserException(message);
    }
}
