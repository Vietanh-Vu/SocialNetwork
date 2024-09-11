package com.example.socialnetwork.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record FormErrorResponse(
        int status,
        String path,
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        Instant timestamp,
        String message,
        List<ValidationError> errors
) {
    @Builder
    public record ValidationError(
            String field,
            List<String> messages
    ) {
    }
}
