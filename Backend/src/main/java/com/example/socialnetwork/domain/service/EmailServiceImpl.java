package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.domain.port.api.EmailServicePort;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.internet.MimeMessage;


import java.util.Map;

@RequiredArgsConstructor
public class EmailServiceImpl implements EmailServicePort {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Async
    @Override
    //dung ham nay de send mail toi nguoi dung, gom co subject, ten mail can xac nhan cung nhu la content cua mailbox
    public void send(String subject, String emailTo, String content) {
        send(subject, emailTo, content, true);
    }

    @Async
    @Override
    public void send(String subject, String emailTo, String template, Map<String, Object> variables) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        String htmlContent = templateEngine.process(template, thymeleafContext);
        send(subject, emailTo, htmlContent, true);
    }

    @Async
    @Override
    public void send(String subject, String emailTo, String content, String attachmentName, InputStreamSource attachment) {
        send(subject, emailTo, content, false, attachmentName, attachment);
    }

    @Async
    @Override
    public void send(String subject, String emailTo, String template, Map<String, Object> variables,
                     String attachmentName, InputStreamSource attachment) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        String htmlContent = templateEngine.process(template, thymeleafContext);
        send(subject, emailTo, htmlContent, true, attachmentName, attachment);
    }

    private void send(String subject, String emailTo, String content, boolean isHtmlFormat) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            message.setSubject(subject);
            message.setText(content, isHtmlFormat);
            message.setTo(emailTo);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email to " + emailTo);
        }
    }

    private void send(String subject, String emailTo, String content, boolean isHtmlFormat, String attachmentName,
                      InputStreamSource attachment) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(content, isHtmlFormat);

            message.addAttachment(attachmentName, attachment);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email to " + emailTo);
        }
    }
}
