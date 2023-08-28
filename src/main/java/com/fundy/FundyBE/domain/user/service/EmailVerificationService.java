package com.fundy.FundyBE.domain.user.service;

import com.fundy.FundyBE.domain.user.service.dto.request.VerifyEmailCodeServiceRequest;
import com.fundy.FundyBE.domain.user.service.dto.response.EmailCodeResponse;
import com.fundy.FundyBE.domain.user.service.dto.response.VerifyEmailResponse;
import com.fundy.FundyBE.global.component.email.AsyncEmailSender;
import com.fundy.FundyBE.global.component.jwt.EmailVerifyJwtProvider;
import com.fundy.FundyBE.global.component.jwt.dto.request.VerifyEmailInfoRequest;
import com.fundy.FundyBE.global.validation.user.UserValidator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final AsyncEmailSender asyncEmailSender;
    private final EmailVerifyJwtProvider emailVerifyJwtProvider;
    private final UserValidator userValidator;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public EmailCodeResponse sendEmailCodeAndReturnToken(String email) {
        userValidator.hasDuplicateEmail(email);
        String code = generateCode();
        String token = emailVerifyJwtProvider.generateToken(email, code);
        sendEmailCode(email, code);

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

    private void sendEmailCode(String email, String code) {
        try {
            asyncEmailSender.sendEmail(createEmailCodeMessage(email, code));
        } catch (MessagingException e) {
            log.warn("이메일 전송에 실패하였습니다.");
        }
    }

    private MimeMessage createEmailCodeMessage(String email, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("이메일 인증 코드");
        message.addRecipients(Message.RecipientType.TO, email);
        message.setText(getEmailCodeContext(email, code), "utf-8", "html");
        return message;
    }

    private String getEmailCodeContext(String email, String code) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        return templateEngine.process("sendCode",context);
    }
}
