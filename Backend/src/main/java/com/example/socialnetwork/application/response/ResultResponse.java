package com.example.socialnetwork.application.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ResultResponse (
    int status,
    String message,
    Object result,
    Instant timestamp
){
}

