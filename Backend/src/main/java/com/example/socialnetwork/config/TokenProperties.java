package com.example.socialnetwork.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(prefix = "token")
@Value
public class TokenProperties {
    String secretKey;
    long accessExpiration;
    long refreshExpiration;
    long verifiedExpiration;
}
