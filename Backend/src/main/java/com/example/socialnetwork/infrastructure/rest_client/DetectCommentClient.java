package com.example.socialnetwork.infrastructure.rest_client;

import com.example.socialnetwork.infrastructure.rest_client.request.DetectCommentRequest;
import com.example.socialnetwork.infrastructure.rest_client.response.DetectCommentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "detect-comment-service", url = "${detect-comment.api.url}")
public interface DetectCommentClient {

  @PostMapping(value = "/api/detect", consumes = "application/json")
  DetectCommentResponse detectHateSpeech(
      @RequestHeader("X-API-KEY") String apiKey,
      @RequestBody DetectCommentRequest request
  );
}