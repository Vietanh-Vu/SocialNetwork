package com.example.socialnetwork.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppealStatusResponse {
  private boolean hasActiveBan;
  private boolean hasPendingAppeal;
  private boolean canCreateAppeal;
}
