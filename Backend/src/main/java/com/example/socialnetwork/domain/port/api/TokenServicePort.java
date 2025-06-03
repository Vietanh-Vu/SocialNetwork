package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.common.constant.TokenType;


public interface TokenServicePort {
    void revokeAllUserTokens(String userId, TokenType tokenType);
    void revokeRefreshToken(String refreshToken, String userId);
    String getTokenInfo(String token, TokenType tokenType);
    Boolean checkTokenBanUser(String token, Long userId, TokenType tokenType);
    void saveToken(String token, String userId, TokenType tokenType, long expiration);
    String getTokenByUserId(String userId, TokenType tokenType);
    void deleteToken(String key);
}
