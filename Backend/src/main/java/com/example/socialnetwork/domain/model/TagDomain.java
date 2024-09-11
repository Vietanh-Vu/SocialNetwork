package com.example.socialnetwork.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDomain {
    private Long id = null;

    private Long userIdTag;

    private Long postId = null;

    private Long userIdTagged;

    private LocalDateTime createdAt;
}
