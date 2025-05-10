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
public class WeeklyCommentResponse {
  private List<WeeklyData> weeklyStats;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WeeklyData {
    private String startDate;
    private String endDate;
    private Long count;
  }
}
