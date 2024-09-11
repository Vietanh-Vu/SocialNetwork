package com.example.socialnetwork.application.response;

import com.example.socialnetwork.common.constant.ECloseRelationship;
import com.example.socialnetwork.common.constant.ERelationship;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendResponse {
    Long id;
    String avatar;
    String username;
    String email;
    int mutualFriends;
    ERelationship status;
    ECloseRelationship closeRelationship;
}
