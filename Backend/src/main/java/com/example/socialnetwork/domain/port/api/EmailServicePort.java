package com.example.socialnetwork.domain.port.api;

import org.springframework.core.io.InputStreamSource;

import java.util.Map;

public interface EmailServicePort {

    void send(String subject, String emailTo, String content);

    void send(String subject, String emailTo, String template, Map<String, Object> variables);

    void send(String subject, String emailTo, String content, String attachmentName, InputStreamSource attachment);

    void send(String subject, String emailTo, String template, Map<String, Object> variables, String attachmentName, InputStreamSource attachment);
}