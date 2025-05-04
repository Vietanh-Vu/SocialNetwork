package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.infrastructure.rest_client.DetectCommentClient;
import com.example.socialnetwork.infrastructure.rest_client.request.DetectCommentRequest;
import com.example.socialnetwork.infrastructure.rest_client.response.DetectCommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentDetectionService {
  @Value("${detect-comment.api.key}")
  private String apiKey;

  private final DetectCommentClient detectCommentClient;

  public DetectCommentResponse detect(String text) {
    try {
      DetectCommentResponse detectCommentResponse =
          detectCommentClient.detectHateSpeech(
              apiKey,
              DetectCommentRequest.builder().text(text).build()
          );

      if (Objects.isNull(detectCommentResponse) ||
          Objects.isNull(detectCommentResponse.getPrediction()) ||
          Objects.isNull(detectCommentResponse.getPrediction().getHateProbability())) {
        log.error(">>> [DetectCommentResponse] Error while calling detect comment {} API: {}", text, detectCommentResponse);
        return null;
      }

      return detectCommentClient.detectHateSpeech(
          apiKey,
          DetectCommentRequest.builder().text(text).build()
      );
    } catch (Exception e) {
      log.error(">>> [DetectCommentResponse] Error while calling detect comment {} API: {}", text, e.getMessage());
      return null;
    }
  }
}
