package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.CloseRelationshipRequest;
import com.example.socialnetwork.application.response.CloseRelationshipResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.CloseRelationship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring", uses = {SecurityUtil.class})
public interface CloseRelationshipMapper {
    CloseRelationshipMapper INSTANCE = Mappers.getMapper(CloseRelationshipMapper.class);


    @Mapping(target = "user.id", expression = "java(getUserId())")
    @Mapping(target = "targetUser.id", source = "userDomain.id")
    @Mapping(target = "targetUser.username", source = "userDomain.username")
    @Mapping(target = "targetUser.avatar", source = "userDomain.avatar")
    @Mapping(target = "createdAt", expression = "java(getCreateAt())")
    CloseRelationshipDomain requestToDomain(CloseRelationshipRequest request, UserDomain userDomain);

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "targetUser.id", target = "targetUser.id")
    CloseRelationship domainToEntity(CloseRelationshipDomain domain);

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "targetUser.id", target = "targetUser.id")
    CloseRelationshipDomain entityToDomain(CloseRelationship relationship);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "targetUser.id", target = "targetUserId")
    @Mapping(source = "targetUser.username", target = "targetUsername")
    CloseRelationshipResponse domainToResponse(CloseRelationshipDomain domain);

    @Named("getUserId")
    default Long getUserId() {
        return SecurityUtil.getCurrentUserId();
    }

    @Named("getCreateAt")
    default Instant getCreateAt() {
        return Instant.now();
    }

}
