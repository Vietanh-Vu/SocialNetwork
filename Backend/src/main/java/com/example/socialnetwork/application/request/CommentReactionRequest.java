package com.example.socialnetwork.application.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReactionRequest {
    private Long commentId;
    private String reactionType;
}
