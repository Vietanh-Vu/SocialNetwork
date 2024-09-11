package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import org.springframework.security.core.userdetails.User;

public interface AuthServicePort {
    void register(RegisterRequest registerRequest);
    void verifyRegisterToken(String token);
    void verifyForgetPassToken(String token);
    void forgotPassword(String email);
    void changePassword(String newPassword, String oldPassword);
    AuthResponse login(AuthRequest authRequest);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
    void logoutAllDevices(User user);
    void resetPasswordWithToken(String token, String newPassword);
}
