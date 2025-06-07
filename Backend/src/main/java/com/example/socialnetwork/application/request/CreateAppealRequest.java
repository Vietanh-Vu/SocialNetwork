package com.example.socialnetwork.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAppealRequest {
  @NotBlank(message = "Reason is required")
  @Size(min = 10, max = 1000, message = "Reason must be between 10 and 1000 characters")
  private String reason;
}