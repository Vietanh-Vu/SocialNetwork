package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.common.util.HandleFile;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.CommentReactionRepository;
import com.example.socialnetwork.infrastructure.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final CommentRepository commentRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final StorageServicePort storageService;
    public CommentDomain commentRequestToCommentDomain(CommentRequest request) {
        String image = HandleFile.loadFileImage(request.getImage(),storageService,1);
        return CommentDomain.builder()
                .user(UserDomain.builder().id(SecurityUtil.getCurrentUserId()).build())
                .post(PostDomain.builder().id(request.getPostId()).build())
                .parentCommentId(request.getParentCommentId())
                .content(request.getContent())
                .image(image)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public Comment commentDomainToCommentEntity(CommentDomain domain) {
        return Comment.builder()
                .id(domain.getCommentId())
                .user(User.builder().id(domain.getUser().getId()).build())
                .post(Post.builder().id(domain.getPost().getId()).build())
                .parentCommentId(domain.getParentCommentId())
                .content(domain.getContent())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .image(domain.getImage())
                .build();
    }

    public CommentDomain commentEntityToCommentDomain(Comment entity) {
        return CommentDomain.builder()
                .commentId(entity.getId())
                .user(UserDomain.builder()
                        .id(entity.getUser().getId())
                        .username(entity.getUser().getUsername())
                        .avatar(entity.getUser().getAvatar())
                        .build())
                .post(PostDomain.builder().id(entity.getPost().getId()).build())
                .parentCommentId(entity.getParentCommentId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .image(entity.getImage())
                .reactCount(commentReactionRepository.countByComment_Id(entity.getId()))
                .build();
    }

    public CommentResponse commentDomainToCommentResponse(CommentDomain domain) {
        boolean reacted = false;
        Long currentUserId = SecurityUtil.getCurrentUserId();
        CommentReaction commentReaction = commentReactionRepository.findByUserIdAndCommentId(currentUserId, domain.getCommentId()).orElse(null);
        if(commentReaction != null) {
            reacted = true;
        }
        return CommentResponse.builder()
                .commentId(domain.getCommentId())
                .userId(domain.getUser().getId())
                .username(domain.getUser().getUsername())
                .avatar(domain.getUser().getAvatar())
                .postId(domain.getPost().getId())
                .parentCommentId(domain.getParentCommentId())
                .numberOfChild(commentRepository.countByParentCommentId(domain.getCommentId()))
                .content(domain.getContent())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .image(domain.getImage())
                .reactCount(domain.getReactCount())
                .isReacted(reacted)
                .build();
    }
}