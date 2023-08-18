package com.fundy.FundyBE.global.exception;

import com.fundy.FundyBE.global.exception.customException.AuthTypeMismatchException;
import com.fundy.FundyBE.global.exception.customException.DuplicateUserException;
import com.fundy.FundyBE.global.exception.customException.NoAuthorityException;
import com.fundy.FundyBE.global.exception.customException.NoUserException;
import com.fundy.FundyBE.global.exception.customException.RefreshTokenException;
import com.fundy.FundyBE.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
                .collect(Collectors.toList()).toString());
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
        return makeResponse(e.getMessage()); // e.getMessage가 null로 적용됨
    }

    private ExceptionResponse makeResponse(String message) {
        return ExceptionResponse.builder()
                .message(message)
                .build();
    }
}