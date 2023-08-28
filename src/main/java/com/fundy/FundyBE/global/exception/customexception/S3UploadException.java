package com.fundy.FundyBE.global.exception.customexception;

public class S3UploadException extends RuntimeException {
    private S3UploadException(String message) { super(message); }

    public final static S3UploadException createBasic() {
        return new S3UploadException("업로드에 문제가 생겼습니다");
    }

    public final static S3UploadException withCustomMessage(String message) {
        return new S3UploadException(message);
    }
}
