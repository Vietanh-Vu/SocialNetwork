package com.example.socialnetwork.domain.port.api;

import org.springframework.security.core.userdetails.User;

public interface JwtServicePort {
    User validateAndExtractUser(String token);
    String generateAccessToken(User user);
    String generateRefreshToken();
    String generateVerifyToken();
    boolean isTokenValid(String token);
}
