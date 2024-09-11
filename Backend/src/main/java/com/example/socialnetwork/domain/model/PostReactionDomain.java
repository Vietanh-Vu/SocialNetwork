package com.example.socialnetwork.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionDomain {
    private Long id = null;
    private Long userId;
    private Long postId;
    private String reactionType;
    private Instant createdAt;
}
