package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.ProfileRequest;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Gender;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.util.HandleFile;
import com.example.socialnetwork.domain.publisher.CustomEventPublisher;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.*;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class UserServiceImpl implements UserServicePort {
    private final EmailServicePort emailService;
    private final UserDatabasePort userDatabase;
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final S3ServicePort s3Service;
    private final StorageServicePort storageService;
    private final CustomEventPublisher customEventPublisher;
    private final StorageServicePort storageServicePort;
    @Value("${link.front-end-domain}")
    private String domain;
    @Value("${link.confirm-email-verify}")
    private String confirmEmailVerifyLink;
    @Value("${link.forgot-password-verify}")
    private String resetPasswordVerifyLink;


    @Override
    public UserDomain getProfile(Long sourceUserId, Long targetUserID) {
        if (targetUserID == null) {
             targetUserID = sourceUserId;
        }

        UserDomain sourceUser = userDatabase.findById(sourceUserId);
        UserDomain targetUser = userDatabase.findById(targetUserID);


        if (sourceUser == null || targetUser == null) {
            throw new NotFoundException("User not found");
        }

        // allow to see the profile
        // 1. get own profile
        // 2. target user is public
        // 3. visibility is FRIEND + relation between source and target is friend
        boolean isOwnProfile = Objects.equals(sourceUserId, targetUserID);
        boolean isPublicProfile = targetUser.getVisibility() == Visibility.PUBLIC;
        boolean isFriendProfile = targetUser.getVisibility() == Visibility.FRIEND &&
                relationshipDatabasePort.getRelationship(sourceUserId, targetUserID) == ERelationship.FRIEND;
        boolean isBlock = relationshipDatabasePort.getRelationship(sourceUserId, targetUserID) == ERelationship.BLOCK;

        if ((isOwnProfile || isPublicProfile || isFriendProfile) && !isBlock) {
            return userDatabase.findById(targetUserID);
        }

        // now allow when target user is private or relation between source and target is block and other case
        throw new NotAllowException("You are not allowed to view this profile");
    }

    @Override
    public void deleteProfile(Long userId) {
        UserDomain user = userDatabase.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        // delete avatar and background image in s3
        if (user.getAvatar() != null) {
            s3Service.deleteFile(user.getAvatar());
        }
        if (user.getBackgroundImage() != null) {
            s3Service.deleteFile(user.getBackgroundImage());
        }

        // delete in database
        userDatabase.deleteById(userId);
    }

    @Override
    public void sendVerificationEmail(UserDomain user, String confirmToken) {
        String subject = "Confirm your email address, " + user.getFirstName() + " " + user.getLastName();
        Map<String, Object> model = new HashMap<>();
        String link = domain + confirmEmailVerifyLink + confirmToken;
        model.put("link", link);
        model.put("name", user.getFirstName());
        emailService.send(subject, user.getEmail(), "email/email-confirmation.html", model);
    }

    @Override
    public void sendEmailResetPassword(UserDomain user, String token) {
        String subject = "Reset your password, " + user.getFirstName();
        Map<String, Object> model = new HashMap<>();
        String link = domain + resetPasswordVerifyLink + token;
        model.put("link", link);
        model.put("name", user.getFirstName());
        emailService.send(subject, user.getEmail(), "email/forgot-password.html", model);
    }

    @Override
    public void updateProfile(Long userId, ProfileRequest profileRequest, Boolean isDeleteAvt, Boolean isDeleteBackground) {
        UserDomain user = userDatabase.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        user.setUsername(profileRequest.getFirstName() + " " + profileRequest.getLastName());
        user.setFirstName(profileRequest.getFirstName());
        user.setLastName(profileRequest.getLastName());
        user.setGender(Gender.valueOf(profileRequest.getGender()));
        user.setVisibility(Visibility.valueOf(profileRequest.getVisibility()));
        user.setBio(profileRequest.getBio());
        user.setLocation(profileRequest.getLocation());
        user.setWork(profileRequest.getWork());
        user.setEducation(profileRequest.getEducation());
        user.setUpdatedAt(Instant.now());
        user.setDateOfBirth(profileRequest.getDateOfBirth());
//        if(!isDeleteAvt){
//            String avt = HandleFile.loadFileImage(profileRequest.getAvatar(), storageServicePort, 1);
//            if(avt != null){
//                if(user.getAvatar() != null && !user.getAvatar().isEmpty()){
//                    s3Service.deleteFile(HandleFile.getFilePath(user.getAvatar()));
//                }
//                user.setAvatar(avt);
//            }
//        }else{
//            if(user.getAvatar() != null && !user.getAvatar().isEmpty()){
//                s3Service.deleteFile(HandleFile.getFilePath(user.getAvatar()));
//
//                user.setAvatar(null);
//            }

        String avt = HandleFile.loadFileImage(profileRequest.getAvatar(), storageServicePort, 1);
        if(isDeleteAvt){
            if(user.getAvatar() != null && !user.getAvatar().isEmpty()){
                s3Service.deleteFile(HandleFile.getFilePath(user.getAvatar()));
                user.setAvatar(avt);
            }
        }else{
            if(avt != null && !avt.isEmpty()) {
                user.setAvatar(avt);
            }
        }


//        if(!isDeleteBackground){
//            String background = HandleFile.loadFileImage(profileRequest.getBackground(), storageServicePort, 1);
//            if(background != null){
//                if(user.getBackgroundImage() != null && !user.getBackgroundImage().isEmpty()){
//                    s3Service.deleteFile(HandleFile.getFilePath(user.getBackgroundImage()));
//                }
//                user.setBackgroundImage(background);
//            }
//        }else{
//            if(user.getBackgroundImage() != null && !user.getBackgroundImage().isEmpty()){
//                s3Service.deleteFile(HandleFile.getFilePath(user.getBackgroundImage()));
//                user.setBackgroundImage(null);
//            }
//        }

        String background = HandleFile.loadFileImage(profileRequest.getBackground(), storageServicePort, 1);
        if(isDeleteBackground){
            if(user.getBackgroundImage() != null && !user.getBackgroundImage().isEmpty()){
                s3Service.deleteFile(HandleFile.getFilePath(user.getBackgroundImage()));
                user.setBackgroundImage(background);
            }
        }else{
            if(background != null && !background.isEmpty()) {
                user.setBackgroundImage(background);
            }
        }

        userDatabase.save(user);
        customEventPublisher.publishProfileUpdatedEvent(userId);
    }



    @Override
    public UserDomain findUserById(Long userId) {
        return userDatabase.findById(userId);
    }
}
