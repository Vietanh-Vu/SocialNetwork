package com.example.socialnetwork.infrastructure.rest_client.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetectCommentResponse {
  private Double normalizedHateProbability;
  private String normalizedText;
  private Double originalHateProbability;
  private String originalText;
  private String timestamp;
}