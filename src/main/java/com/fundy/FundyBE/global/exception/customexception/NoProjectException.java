package com.fundy.FundyBE.global.exception.customexception;

public class NoProjectException extends RuntimeException {
    private NoProjectException(String message) {
        super(message);
    }

    public final static NoProjectException createBasic() {
        return new NoProjectException("서버 내부에서 프로젝트 관련 에러가 발생하였습니다");
    }

    public final static NoProjectException withCustomMessage(String message) {
        return new NoProjectException(message);
    }
}