package com.example.socialnetwork.infrastructure.kafka.event.suggestion.impl;

import com.example.socialnetwork.infrastructure.kafka.event.EventType;
import com.example.socialnetwork.infrastructure.kafka.event.suggestion.UserInteractionEvent;

public class FriendAcceptEvent extends UserInteractionEvent {
  public FriendAcceptEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.FRIEND_REQUEST_ACCEPTED);
  }
}
