package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.PostReactionRequest;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
@Mapper
public interface PostReactionMapper {
    PostReactionMapper INSTANCE = Mappers.getMapper(PostReactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", expression = "java(getUserId())")
    @Mapping(target = "createdAt", expression = "java(getCreateAt())")
    PostReactionDomain requestToDomain(PostReactionRequest postReactionRequest);


    @org.mapstruct.Named("getUserId")
    default Long getUserId() {
        return SecurityUtil.getCurrentUserId();
    }
    @org.mapstruct.Named("getCreateAt")
    default Instant getCreateAt() {
        return Instant.now();
    }

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    PostReaction domainToEntity(PostReactionDomain postReactionDomain);

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    PostReactionDomain entityToDomain(PostReaction postReaction);

    PostReactionResponse domainToResponse(PostReactionDomain postReactionDomain);



    @Mapping(source = "postReactionDomain.id", target = "id")
    @Mapping(source = "postReactionDomain.userId", target = "userId")
    @Mapping(source = "postReactionDomain.postId", target = "postId")
    @Mapping(source = "postReactionDomain.reactionType", target = "reactionType")
    @Mapping(source = "postReactionDomain.createdAt", target = "createdAt")
    @Mapping(source = "userDomain.username", target = "username")
    @Mapping(source = "userDomain.avatar", target = "avatar")
    PostReactionResponse domainToResponseWithUser(PostReactionDomain postReactionDomain, UserDomain userDomain);
}
