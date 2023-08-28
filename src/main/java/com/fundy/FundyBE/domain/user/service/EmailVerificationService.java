package com.fundy.FundyBE.domain.user.service;

import com.fundy.FundyBE.domain.user.service.dto.request.VerifyEmailCodeServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.EmailCodeResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.VerifyEmailResponse;
import com.fundy.FundyBE.global.component.email.AsyncEmailSender;
import com.fundy.FundyBE.global.component.jwt.EmailVerifyJwtProvider;
import com.fundy.FundyBE.global.component.jwt.dto.request.VerifyEmailInfoRequest;
import com.fundy.FundyBE.global.validation.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final AsyncEmailSender asyncEmailSender;
    private final EmailVerifyJwtProvider emailVerifyJwtProvider;
    private final UserValidator userValidator;

    public EmailCodeResponse sendEmailCodeAndReturnToken(String email) {
        userValidator.hasDuplicateEmail(email);
        String code = generateCode();
        String token = emailVerifyJwtProvider.generateToken(email, code);
        asyncEmailSender.sendEmailCode(email, code);

        return EmailCodeResponse.builder()
                .email(email)
                .token(token)
                .build();
    }

    public VerifyEmailResponse verifyTokenWithEmail(final VerifyEmailCodeServiceRequest verifyEmailCodeServiceRequest) {
        VerifyEmailInfoRequest verifyEmailInfoRequest = VerifyEmailInfoRequest.builder()
                .email(verifyEmailCodeServiceRequest.getEmail())
                .token(verifyEmailCodeServiceRequest.getToken())
                .code(verifyEmailCodeServiceRequest.getCode())
                .build();

        return VerifyEmailResponse.builder()
                .email(verifyEmailCodeServiceRequest.getEmail())
                .verify(emailVerifyJwtProvider.isVerifyToken(verifyEmailInfoRequest))
                .build();
    }

    private String generateCode() {
        int codeSize = 6;
        String code = "";
        String numericCharacters = "0123456789";
        Random random = new Random();
        for(int i=0;i<codeSize;i++) {
            code += numericCharacters.charAt(random.nextInt(numericCharacters.length()));
        }
        return code;
    }
}
