package com.example.socialnetwork.domain.model;


import com.example.socialnetwork.common.constant.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDomain {
    private Long id;

    private Long userId;

    private String content;

    private Visibility visibility;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant lastComment = null;

    private String photoLists;

    private  Long numberOfComments;

    private Long numberOfReacts;

    private List<TagDomain> tagDomains;
}