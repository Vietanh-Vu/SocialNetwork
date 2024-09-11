package com.example.socialnetwork.application.response;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private Long userId;
    private String username;
    private String avatar;
    private Long postId;
    private Long parentCommentId;
    private Long numberOfChild;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private String image;
    private Long reactCount;
    private Boolean isReacted = false;
}
