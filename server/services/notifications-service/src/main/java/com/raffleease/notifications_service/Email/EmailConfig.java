package com.raffleease.notifications_service.Email;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private final JavaMailSenderImpl mailSender;
    private final Environment env;

    @PostConstruct
    public void init() {
        String mailPassword = env.getProperty("mail_password");
        mailSender.setPassword(mailPassword);
    }
}