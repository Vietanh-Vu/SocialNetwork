package com.example.socialnetwork.domain.model;

import com.example.socialnetwork.common.constant.AppealStatus;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentBanAppealDomain {
  private Long appealId;
  private Long userId;
  private String username;
  private String userAvatar;
  private String reason;
  private AppealStatus status;
  private String adminResponse;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant resolvedAt;
}