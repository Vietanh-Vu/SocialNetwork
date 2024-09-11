package com.example.socialnetwork.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(
        Map<String, String> headers
) {
}
