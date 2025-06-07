package com.example.socialnetwork.application.response;

import com.example.socialnetwork.common.constant.AppealStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentBanAppealResponse {
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
