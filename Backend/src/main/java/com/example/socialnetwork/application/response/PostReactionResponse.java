package com.example.socialnetwork.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReactionResponse {
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private Long postId;
    private String reactionType;
    private Instant createdAt;
}
