package com.example.socialnetwork.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopViolatingUsersResponse {
  private List<ViolatorData> violators;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ViolatorData {
    private Long userId;
    private String username;
    private Long commentCount;
    private String avatar;
    private boolean isBanned;
  }
}