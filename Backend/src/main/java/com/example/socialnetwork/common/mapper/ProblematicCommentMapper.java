package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.ProblematicCommentResponse;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.ProblematicCommentDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.ProblematicComment;
import com.example.socialnetwork.infrastructure.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProblematicCommentMapper {
    public ProblematicCommentDomain problematicCommentEntityToProblematicCommentDomain(ProblematicComment entity) {
        return ProblematicCommentDomain.builder()
                .id(entity.getId())
                .user(UserDomain.builder()
                        .id(entity.getUser().getId())
                        .username(entity.getUser().getUsername())
                        .avatar(entity.getUser().getAvatar())
                        .build())
                .content(entity.getContent())
                .spamProbability(entity.getSpamProbability())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ProblematicComment problematicCommentDomainToProblematicCommentEntity(ProblematicCommentDomain domain) {
        return ProblematicComment.builder()
                .id(domain.getId())
                .user(User.builder()
                        .id(domain.getUser().getId())
                        .build())
                .content(domain.getContent())
                .spamProbability(domain.getSpamProbability())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public ProblematicCommentResponse toProblematicCommentResponse(ProblematicCommentDomain domain) {
        return ProblematicCommentResponse.builder()
            .id(domain.getId())
            .userId(domain.getUser().getId())
            .username(domain.getUser().getUsername())
            .userAvatar(domain.getUser().getAvatar())
            .content(domain.getContent())
            .spamProbability(domain.getSpamProbability())
            .createdAt(domain.getCreatedAt())
            .build();
    }
}
