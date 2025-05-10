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
public class DashboardStatResponse {
  private Long todayCount;
  private Long weeklyCount;
  private Long monthlyCount;
  private Long totalCount;
  private List<TopViolatorResponse> topViolators;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TopViolatorResponse {
    private Long userId;
    private String username;
    private Long commentCount;
  }
}
