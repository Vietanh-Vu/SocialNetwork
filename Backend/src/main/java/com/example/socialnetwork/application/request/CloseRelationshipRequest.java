package com.example.socialnetwork.application.request;

import com.example.socialnetwork.common.constant.ECloseRelationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseRelationshipRequest {
    private Long targetUserId;
    private ECloseRelationship closeRelationshipName;
}
