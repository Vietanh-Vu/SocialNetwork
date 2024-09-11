package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.ProfileRequest;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserServicePort {
    UserDomain getProfile(Long sourceUserId, Long targetUserID);
    void deleteProfile(Long userId);
    void sendVerificationEmail(UserDomain user, String confirmToken);
    void sendEmailResetPassword(UserDomain user, String resetToken);
    void updateProfile(Long userId, ProfileRequest profileRequest, Boolean isDeleteAvt, Boolean isDeleteBackground);
    UserDomain findUserById(Long userId);
}
