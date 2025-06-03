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
public class UserViolationStatsResponse {
  private Long userId;
  private Long totalCount;
  private List<MonthlyViolationData> monthlyStats;
}
