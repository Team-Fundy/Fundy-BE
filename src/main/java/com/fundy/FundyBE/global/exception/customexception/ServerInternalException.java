package com.fundy.FundyBE.global.exception.customexception;

public class ServerInternalException extends RuntimeException {
    private ServerInternalException(String message) { super(message);}
    public static ServerInternalException createBasic() {
        return new ServerInternalException("서버 내부 에러");
    }
}
