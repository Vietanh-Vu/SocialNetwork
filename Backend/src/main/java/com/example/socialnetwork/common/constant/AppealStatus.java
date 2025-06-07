package com.example.socialnetwork.common.constant;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AppealStatus {
  PENDING("PENDING"),
  APPROVED("APPROVED"),
  REJECTED("REJECTED");
  private final String status;

  public static List<String> getAllStatuses() {
    return List.of(PENDING.getStatus(), APPROVED.getStatus(), REJECTED.getStatus());
  }
}
