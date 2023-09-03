package com.fundy.FundyBE.global.exception;

import com.fundy.FundyBE.global.exception.customexception.AuthTypeMismatchException;
import com.fundy.FundyBE.global.exception.customexception.DuplicateUserException;
import com.fundy.FundyBE.global.exception.customexception.InvalidPeriodException;
import com.fundy.FundyBE.global.exception.customexception.NoAuthorityException;
import com.fundy.FundyBE.global.exception.customexception.NoProjectException;
import com.fundy.FundyBE.global.exception.customexception.NoUserException;
import com.fundy.FundyBE.global.exception.customexception.RefreshTokenException;
import com.fundy.FundyBE.global.exception.customexception.S3UploadException;
import com.fundy.FundyBE.global.exception.customexception.ServerInternalException;
import com.fundy.FundyBE.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return makeResponse(e.getBindingResult()
                .getFieldErrors()
                .stream().map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList()));
    }

    @ExceptionHandler({DuplicateUserException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleDuplicateUserException(final DuplicateUserException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({NoAuthorityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleNoAuthorityException(final NoAuthorityException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({NoUserException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public final ExceptionResponse handleNoUserException(final NoUserException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({RefreshTokenException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleRefreshTokenException(final RefreshTokenException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({AuthTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleAuthTypeMismatchException(final AuthTypeMismatchException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({SizeLimitExceededException.class})
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ResponseBody
    public final ExceptionResponse handleSizeLimitExceededException(final SizeLimitExceededException e) {
        return makeResponse("파일의 사이즈가 너무 큽니다");
    }

    @ExceptionHandler({S3UploadException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleS3UploadException(final S3UploadException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({InvalidPeriodException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleInvalidPeriodException(final InvalidPeriodException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({NoProjectException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final ExceptionResponse handleProjectException(final NoProjectException e) {
        return makeResponse(e.getMessage());
    }

    @ExceptionHandler({ServerInternalException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final ExceptionResponse handleServerInternalException(final ServerInternalException e) {
        return makeResponse(e.getMessage());
    }

    private ExceptionResponse<List<String>> makeResponse(List<String> messages) {
        return ExceptionResponse.<List<String>>builder()
                .message(messages)
                .build();
    }

    private ExceptionResponse<String> makeResponse(String message) {
        return ExceptionResponse.<String>builder()
                .message(message)
                .build();
    }
}