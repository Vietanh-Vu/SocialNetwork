package org.example.kafkaconsumer.share.mapper;

import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.example.kafkaconsumer.share.enums.Visibility;
import org.example.kafkaconsumer.usecase.dto.UserDto;

public class UserMapper {
  public static UserDocument userDtoToDocument(UserDto userDto) {
    return UserDocument.builder()
        .userId(userDto.getUserId())
        .username(userDto.getUsername())
        .email(userDto.getEmail())
        .password(userDto.getPassword())
        .firstName(userDto.getFirstName())
        .lastName(userDto.getLastName())
        .gender(userDto.getGender())
        .visibility(userDto.getVisibility() != null ? userDto.getVisibility().name() : null)
        .role(userDto.getRoleId() != null ? userDto.getRoleId().toString() : null)
        .bio(userDto.getBio())
        .location(userDto.getLocation())
        .work(userDto.getWork())
        .education(userDto.getEducation())
//        .createdAt(user.getCreatedAt())
//        .updatedAt(user.getUpdatedAt())
        .avatar(userDto.getAvatar())
        .backgroundImage(userDto.getBackgroundImage())
//        .dateOfBirth(user.getDateOfBirth())
        .isEmailVerified(userDto.getIsEmailVerified())
        .friendIds(userDto.getFriendIds())
        .build();
  }

  public static UserDto userEntityToDto(User user) {
    return UserDto.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .gender(user.getGender())
        .visibility(Visibility.valueOf(user.getVisibility()))
        .roleId(user.getRole().getId())
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
        .build();
  }
}
