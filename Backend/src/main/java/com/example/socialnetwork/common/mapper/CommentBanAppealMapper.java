package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.CommentBanAppealResponse;
import com.example.socialnetwork.common.constant.AppealStatus;
import com.example.socialnetwork.domain.model.CommentBanAppealDomain;
import com.example.socialnetwork.infrastructure.entity.CommentBanAppeal;
import org.springframework.stereotype.Component;

@Component
public class CommentBanAppealMapper {

  public CommentBanAppealDomain toCommentBanAppealDomain(CommentBanAppeal commentBanAppeal) {
    if (commentBanAppeal == null) {
      return null;
    }

    return CommentBanAppealDomain.builder()
        .appealId(commentBanAppeal.getId())
        .userId(commentBanAppeal.getUser() != null ? commentBanAppeal.getUser().getId() : null)
        .username(
            commentBanAppeal.getUser() != null ? commentBanAppeal.getUser().getUsername() : null)
        .userAvatar(
            commentBanAppeal.getUser() != null ? commentBanAppeal.getUser().getAvatar() : null)
        .reason(commentBanAppeal.getReason())
        .status(
            commentBanAppeal.getStatus() != null
                ? AppealStatus.valueOf(commentBanAppeal.getStatus())
                : null)
        .adminResponse(commentBanAppeal.getAdminResponse())
        .createdAt(commentBanAppeal.getCreatedAt())
        .updatedAt(commentBanAppeal.getUpdatedAt())
        .resolvedAt(commentBanAppeal.getResolvedAt())
        .build();
  }

  public CommentBanAppealResponse toCommentBanAppealResponse(CommentBanAppealDomain domain) {
    if (domain == null) {
      return null;
    }

    return CommentBanAppealResponse.builder()
        .appealId(domain.getAppealId())
        .userId(domain.getUserId())
        .username(domain.getUsername())
        .userAvatar(domain.getUserAvatar())
        .reason(domain.getReason())
        .status(domain.getStatus() != null ? AppealStatus.valueOf(domain.getStatus().name()) : null)
        .adminResponse(domain.getAdminResponse())
        .createdAt(domain.getCreatedAt())
        .updatedAt(domain.getUpdatedAt())
        .resolvedAt(domain.getResolvedAt())
        .build();
  }
}
