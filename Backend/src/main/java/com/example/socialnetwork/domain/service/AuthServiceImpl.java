package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import com.example.socialnetwork.common.constant.ERole;
import com.example.socialnetwork.common.constant.TokenType;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.AuthServicePort;
import com.example.socialnetwork.domain.port.api.JwtServicePort;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.domain.publisher.CustomEventPublisher;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.DuplicateException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServicePort {
    private final JwtServicePort jwtService;
    private final TokenServicePort tokenService;
    private final UserServicePort userService;
    private final UserDatabasePort userDatabase;
    private final AuthenticationManager authenticationManager;
    private final CustomEventPublisher customEventPublisher;
    @Value("${token.verified-expiration}")
    private long verifyExpiration;
    @Value("${token.refresh-expiration}")
    private long refreshExpiration;

    @Override
    public void register(RegisterRequest registerRequest) {
        UserDomain user = userDatabase.findByEmail(registerRequest.getEmail());
        if (user == null) {
            user = userDatabase.createUser(registerRequest);
        } else {
            boolean isEmailVerified = user.getIsEmailVerified();
            boolean isExitVerifyToken = tokenService.getTokenByUserId(String.valueOf(user.getId()), TokenType.VERIFIED) != null; // true = exist
            // user is not verified and there is no token in the database => create new user
            if (!isEmailVerified && !isExitVerifyToken) {
                userDatabase.deleteById(user.getId());
                user = userDatabase.createUser(registerRequest);
            } else {
                throw new DuplicateException("This email is being used by another user");
            }
        }

        String confirmToken = jwtService.generateVerifyToken();
        System.out.println(confirmToken);
        tokenService.saveToken(confirmToken, String.valueOf(user.getId()), TokenType.VERIFIED, verifyExpiration);
        userService.sendVerificationEmail(user, confirmToken);
    }

    @Override
    @Transactional
    public void verifyRegisterToken(String token) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);
        UserDomain user = userDatabase.findById(Long.parseLong(userId));
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        user.setIsEmailVerified(true);
        userDatabase.save(user);
        customEventPublisher.publishRegisterEvent(user.getId());
        tokenService.revokeAllUserTokens(String.valueOf(user.getId()), TokenType.VERIFIED);
    }

    @Override
    public void verifyForgetPassToken(String token) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);

        if (userId == null) {
            throw new NotFoundException("Invalid token");
        }
    }


    @Override
    public void forgotPassword(String email) {
        UserDomain user = userDatabase.findByEmail(email);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!user.getIsEmailVerified()) {
            throw new NotFoundException("Email is not verified or being used by another user");
        }

        String resetToken = jwtService.generateVerifyToken();
        tokenService.saveToken(resetToken, String.valueOf(user.getId()), TokenType.VERIFIED, verifyExpiration);
        userService.sendEmailResetPassword(user, resetToken);
    }

    @Override
    public void changePassword(String newPassword, String oldPassword) {

        Long currentUserId = SecurityUtil.getCurrentUserId();

        UserDomain currentUser = userDatabase.findById(Long.parseLong(String.valueOf(currentUserId)));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Check if the old password matches the one in the database
        if (!encoder.matches(oldPassword, currentUser.getPassword())) {
            throw new ClientErrorException("Old password does not match");
        }

        tokenService.revokeAllUserTokens(String.valueOf(currentUserId), TokenType.REFRESH);
        String hashedPassword = encoder.encode(newPassword);
        userDatabase.updatePassword(currentUserId, hashedPassword);
    }


    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()));

        User user = (User) authentication.getPrincipal();

        boolean isEmailVerify = userDatabase.findById(Long.parseLong(user.getUsername())).getIsEmailVerified();
        if (!isEmailVerify) {
            throw new NotFoundException("Email is not verified");
        } else {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken();
            tokenService.saveToken(refreshToken, user.getUsername(), TokenType.REFRESH, refreshExpiration );
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String userId = tokenService.getTokenInfo(refreshToken, TokenType.REFRESH);

        UserDomain user = userDatabase.findById(Long.parseLong(userId));

        UserDetails userDetails = User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(String.valueOf(ERole.USER))
                .build();

        // Remove the refreshToken from Redis
        tokenService.revokeAllUserTokens(user.getUsername(), TokenType.REFRESH);

        // Generate new accessToken
        String newAccessToken = jwtService.generateAccessToken((User) userDetails);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        tokenService.revokeRefreshToken(refreshToken, String.valueOf(currentUserId));
    }

    @Override
    public void logoutAllDevices(User user) {
        tokenService.revokeAllUserTokens(user.getUsername(), TokenType.REFRESH);
    }

    @Override
    public void resetPasswordWithToken(String token, String newPassword) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);

        UserDomain user = userDatabase.findById(Long.parseLong(userId));

        if (user == null) {
            throw new NotFoundException("User not found or invalid token");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(newPassword);
        userDatabase.updatePassword(Long.valueOf(userId), hashedPassword);
        tokenService.revokeAllUserTokens(String.valueOf(user.getId()), TokenType.REFRESH);
    }
}
