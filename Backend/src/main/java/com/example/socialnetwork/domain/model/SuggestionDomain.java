package com.example.socialnetwork.domain.model;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuggestionDomain {
    Long id;

    UserDomain user;

    UserDomain friend;

    int point;

    int mutualFriends;

    Status status;
}
