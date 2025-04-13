package org.example.kafkaconsumer.share.mapper;

import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.usecase.dto.User;

public class UserMapper {
  public static UserDocument toDocument(User user) {
    return UserDocument.builder()
        .userId(user.getUserId())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .gender(user.getGender())
        .visibility(user.getVisibility() != null ? user.getVisibility().name() : null)
        .role(user.getRoleId() != null ? user.getRoleId().toString() : null)
        .bio(user.getBio())
        .location(user.getLocation())
        .work(user.getWork())
        .education(user.getEducation())
//        .createdAt(user.getCreatedAt())
//        .updatedAt(user.getUpdatedAt())
        .avatar(user.getAvatar())
        .backgroundImage(user.getBackgroundImage())
//        .dateOfBirth(user.getDateOfBirth())
        .isEmailVerified(user.getIsEmailVerified())
        .friendIds(user.getFriendIds())
        .build();
  }
}
