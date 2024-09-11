package com.example.socialnetwork.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionDomain {
    private Long id;
    private UserDomain user;
    private CommentDomain comment;
    private String reactionType;
    private Instant createdAt;
}
