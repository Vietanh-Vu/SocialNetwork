package com.example.socialnetwork.application.response;

import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionResponse {
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private Long commentId;
    private String reactionType;
    private Instant createdAt;
}
