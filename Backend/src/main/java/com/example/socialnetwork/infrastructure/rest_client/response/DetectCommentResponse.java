package com.example.socialnetwork.infrastructure.rest_client.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetectCommentResponse {
  private String text;
  private Prediction prediction;
  private String timestamp;

  @Data
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Prediction {
    private Double hateProbability;
    private Double cleanProbability;
  }
}
