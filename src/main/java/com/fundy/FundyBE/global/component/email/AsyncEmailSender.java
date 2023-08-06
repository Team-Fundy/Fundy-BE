package com.fundy.FundyBE.global.component.email;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncEmailSender {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Async("ThreadPoolTaskExecutor")
    public void sendEmailCode(String email, String token) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {

            message.setSubject("이메일 인증 코드");
            message.addRecipients(
                    Message.RecipientType.TO, email);
            message.setText(
                    getEmailCodeContext(email,token),
                    "utf-8",
                    "html"
            );
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.warn("이메일 전송 실패");
            e.printStackTrace();
        }
    }

    private String getEmailCodeContext(String email, String code) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        return templateEngine.process("sendCode",context);
    }

}
