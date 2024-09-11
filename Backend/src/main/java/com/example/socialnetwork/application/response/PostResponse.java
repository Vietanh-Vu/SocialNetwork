package com.example.socialnetwork.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;

    private Long userId;

    private String username;

    private String avatar;

    private String content;

    private String visibility;

    private Instant createdAt;

    private Instant updatedAt;

//    private List<String> photoLists;
//
//    private Map<String, String> photoMaps = null;

    private List<PhotoResponse> photoResponses = null;

    private  Long numberOfComments;

    private Long numberOfReacts;

    private List<TagUserResponse> tagUsers = null;

    private Boolean isReacted = false;
}