package com.example.socialnetwork.infrastructure.kafka.event.suggestion.impl;

import com.example.socialnetwork.infrastructure.kafka.event.suggestion.SuggestionEvent;
import com.example.socialnetwork.infrastructure.kafka.event.EventType;

public class FriendAcceptEvent extends SuggestionEvent {
  public FriendAcceptEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.FRIEND_REQUEST_ACCEPTED);
  }
}