package com.fundy.FundyBE.global.exception;

import com.fundy.FundyBE.global.exception.customException.CustomAuthorizationException;
import com.fundy.FundyBE.global.exception.customException.DuplicateUserException;
import com.fundy.FundyBE.global.exception.customException.NoAuthorityException;
import com.fundy.FundyBE.global.exception.customException.NoUserException;
import com.fundy.FundyBE.global.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ExceptionResponse.builder()
                .message(e.getBindingResult()
                        .getFieldErrors()
                        .stream().map(fieldError -> fieldError.getDefaultMessage())
                        .collect(Collectors.toList()).toString())
                .build();
    }

    @ExceptionHandler({DuplicateUserException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleDuplicateUserException(final DuplicateUserException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage()).build();
    }

    @ExceptionHandler({NoAuthorityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleNoAuthorityException(final NoAuthorityException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({CustomAuthorizationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ExceptionResponse handleCustomAuthorizationException(final CustomAuthorizationException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({NoUserException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public final ExceptionResponse handleNoUserException(final NoUserException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
    }
}