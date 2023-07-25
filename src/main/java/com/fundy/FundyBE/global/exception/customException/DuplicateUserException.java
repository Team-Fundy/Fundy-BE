package com.fundy.FundyBE.global.exception.customException;
public class DuplicateUserException extends RuntimeException{
    private DuplicateUserException(String message) {
        super(message);
    }

    public final static DuplicateUserException createBasic() {
        return new DuplicateUserException("중복된 유저가 존재합니다.");
    }

    public final static DuplicateUserException withCustomMessage(String message) {
        return new DuplicateUserException(message);
    }
}
