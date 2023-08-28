package com.fundy.FundyBE.global.component.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncEmailSender {
    private final JavaMailSender javaMailSender;
    @Async("ThreadPoolTaskExecutor")
    public void sendEmail(MimeMessage message) {
        javaMailSender.send(message);
    }
}
