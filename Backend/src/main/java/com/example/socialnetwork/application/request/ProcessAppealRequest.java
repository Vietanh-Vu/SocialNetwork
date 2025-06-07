package com.example.socialnetwork.application.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProcessAppealRequest {
  @NotNull(message = "Decision is required")
  private Boolean approved;

  private String adminResponse;
}