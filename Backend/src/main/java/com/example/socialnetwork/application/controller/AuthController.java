package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import com.example.socialnetwork.common.ValidationRegex;
import com.example.socialnetwork.domain.port.api.AuthServicePort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController extends BaseController {
    private final AuthServicePort authService;

    @PostMapping("/register") ///
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        authService.register(registerRequest);
        return buildResponse("Please check your email to verify your account");
    }

    @PostMapping("register/verify")
    public ResponseEntity<?> verifyRegisterToken(
            @RequestParam("token") String token
    ) {
        authService.verifyRegisterToken(token);
        return buildResponse("Token is valid");
    }

    @PostMapping("/forgot_pass")
    public ResponseEntity<?> forgotPassword(
            @Email(message = "Email should be valid", regexp = ValidationRegex.EMAIL_REGEX)
            @RequestParam("email") String email
    ) {
        authService.forgotPassword(email);
        return buildResponse("Reset password request has been sent to your email");
    }

    @PostMapping("/verify_forgot_pass")
    public ResponseEntity<?> verifyForgotPasswordToken(
            @RequestParam("token") String token
    ) {
        authService.verifyForgetPassToken(token);
        return buildResponse("Token is valid");
    }

    @PostMapping("/reset_pass")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @NotBlank(message = "New password cannot be blank")
            @Pattern(message = "Password should be valid", regexp = ValidationRegex.PASSWORD_REGEX)
            @RequestParam(value = "new_password") String newPassword
    ) {
        authService.resetPasswordWithToken(token, newPassword);
        return buildResponse("Password has been reset");
    }

    @PostMapping("/change_pass") ///
    public ResponseEntity<?> changePassword(
            @NotBlank(message = "New password cannot be blank")
            @Pattern(message = "Password should be valid", regexp = ValidationRegex.PASSWORD_REGEX)
            @RequestParam(value = "new_password") String newPassword,
            @RequestParam(value = "old_password") String oldPassword
    ) {
        authService.changePassword(newPassword, oldPassword);
        return buildResponse("Change password successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        AuthResponse authResponse = authService.login(authRequest);
        return buildResponse("Successfully logged in", authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestParam("refresh_token") String refreshToken
    ) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return buildResponse("Successfully refreshed token", authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestParam("refresh_token") String refreshToken
    ) {
        authService.logout(refreshToken);
        return buildResponse("Logout successfully");
    }

    @PostMapping("/logout/all")
    public ResponseEntity<?> logoutAllDevices(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.logoutAllDevices(user);
        return buildResponse("Logout from all devices successfully");
    }
}