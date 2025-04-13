package com.example.socialnetwork.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProblematicCommentDomain {
    private Long id;
    private UserDomain user;
    private String content;
    private Double spamProbability;
    private Instant createdAt;
}
