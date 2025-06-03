package com.example.socialnetwork.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBanInfo {
  private Long userId;
  private String username;
  private String avatar;
  private Instant bannedUntil;
}
