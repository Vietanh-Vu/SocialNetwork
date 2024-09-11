package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.CommentReactionRequest;
import com.example.socialnetwork.application.response.CommentReactionResponse;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper
public interface CommentReactionMapper {
    CommentReactionMapper INSTANCE = Mappers.getMapper(CommentReactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", expression = "java(getUserId())")
    @Mapping(source = "commentId", target = "comment.commentId")
    @Mapping(source = "reactionType", target = "reactionType")
    @Mapping(target = "createdAt", expression = "java(getCreateAt())")
    CommentReactionDomain requestToDomain(CommentReactionRequest commentReactionRequest);

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "comment.commentId", target = "comment.id")
    CommentReaction domainToEntity(CommentReactionDomain commentReactionDomain);


    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "comment.id", target = "comment.commentId")
    CommentReactionDomain entityToDomain(CommentReaction commentReaction);

    @Mapping(source = "commentReactionDomain.id", target = "id")
    @Mapping(source = "commentReactionDomain.user.id", target = "userId")
    @Mapping(source = "commentReactionDomain.reactionType", target = "reactionType")
    @Mapping(source = "commentReactionDomain.createdAt", target = "createdAt")
    @Mapping(source = "userDomain.username", target = "username")
    @Mapping(source = "userDomain.avatar", target = "avatar")
    @Mapping(source = "commentReactionDomain.comment.commentId", target = "commentId")
    CommentReactionResponse domainToResponse(CommentReactionDomain commentReactionDomain, UserDomain userDomain);

//    @Mapping(source = "postReactionDomain.id", target = "id")
//    @Mapping(source = "postReactionDomain.userId", target = "userId")
//    @Mapping(source = "postReactionDomain.postId", target = "postId")
//    @Mapping(source = "postReactionDomain.reactionType", target = "reactionType")
//    @Mapping(source = "postReactionDomain.createdAt", target = "createdAt")
//    @Mapping(source = "userDomain.username", target = "username")
//    @Mapping(source = "userDomain.avatar", target = "avatar")
//    PostReactionResponse domainToResponseWithUser(PostReactionDomain postReactionDomain, UserDomain userDomain);


    @Named("getUserId")
    default Long getUserId() {
        return SecurityUtil.getCurrentUserId();
    }

    @Named("getCreateAt")
    default Instant getCreateAt() {
        return Instant.now();
    }
}