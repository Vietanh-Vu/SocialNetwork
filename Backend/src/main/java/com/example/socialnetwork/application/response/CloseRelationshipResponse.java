package com.example.socialnetwork.application.response;

import com.example.socialnetwork.common.constant.ECloseRelationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseRelationshipResponse {
    private Long userId;

    private Long targetUserId;

    private String targetUsername;

    private ECloseRelationship closeRelationshipName;

    private Instant createdAt;

}
