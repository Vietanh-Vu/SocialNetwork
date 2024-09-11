package com.example.socialnetwork.domain.model;

import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDomain {
    private Long commentId;
    private UserDomain user;
    private PostDomain post;
    private Long parentCommentId;
    private Long numberOfChild;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isHidden;
    private String image;
    private Long reactCount;
}
