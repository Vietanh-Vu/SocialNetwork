package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.ProfileResponse;
import com.example.socialnetwork.common.constant.ERole;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.Role;
import com.example.socialnetwork.infrastructure.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default User toUser(UserDomain userDomain) {
        return User.builder()
                .id(userDomain.getId())
                .username(userDomain.getUsername())
                .password(userDomain.getPassword())
                .email(userDomain.getEmail())
                .firstName(userDomain.getFirstName())
                .lastName(userDomain.getLastName())
                .bio(userDomain.getBio())
                .gender(userDomain.getGender())
                .dateOfBirth(userDomain.getDateOfBirth())
                .location(userDomain.getLocation())
                .work(userDomain.getWork())
                .education(userDomain.getEducation())
                .avatar(userDomain.getAvatar())
                .backgroundImage(userDomain.getBackgroundImage())
                .visibility(userDomain.getVisibility().name())
                .createdAt(userDomain.getCreatedAt())
                .updatedAt(userDomain.getUpdatedAt())
                .isEmailVerified(userDomain.getIsEmailVerified())
                .build();
    }

    ProfileResponse toProfileResponse(UserDomain userDomain);

    UserDomain toUserDomain(User user);

    List<UserDomain> toUserDomains(List<User> users);

    default Role toRole(ERole ERole){
        Role role = new Role();
        role.setName(ERole.name());
        return role;
    }

    default ERole toERole(Role Role){
        return ERole.valueOf(Role.getName());
    }
}