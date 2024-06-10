package com.shan.library.service.impl;

import com.shan.library.service.intf.IEmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService implements IEmailService {
    private static final String RESET_PASSWORD_TEMPLATE = "password-reset";

    @Value("${spring.mail.username}")
    private String from;
    @Value("${app.password.reset.uri}")
    private String resetPasswordURI;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @SneakyThrows
    public void sendPasswordResetEmail(@NonNull String email, @NonNull String token) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        Context context = new Context();
        String uri = resetPasswordURI + token;
        context.setVariable("uri", uri);
        String htmlContent = templateEngine.process(RESET_PASSWORD_TEMPLATE, context);
        helper.setTo(email);
        helper.setFrom(from);
        helper.setSubject("Password Reset Request");
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }
}
