package com.example.socialnetwork.application.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProblematicCommentResponse {
  private Long id;
  private Long userId;
  private String username;
  private String userAvatar;
  private String content;
  private Double spamProbability;
  private Instant createdAt;
}