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
public class MonthlyCommentResponse {
  private List<MonthlyData> monthlyStats;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MonthlyData {
    private Integer month;
    private Integer year;
    private Long count;
  }
}