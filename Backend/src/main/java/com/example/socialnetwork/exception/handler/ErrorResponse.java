package com.example.socialnetwork.exception.handler;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        int status,
        String path,
        Instant timestamp,
        String message
) {
}


