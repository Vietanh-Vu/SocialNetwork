package com.example.socialnetwork.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfigCreateRequest {
  @NotBlank(message = "Name is required")
  @Size(max = 255, message = "Name must be less than 255 characters")
  private String name;

  @NotBlank(message = "Code is required")
  @Size(max = 50, message = "Code must be less than 50 characters")
  private String code;

  private String value;
}
