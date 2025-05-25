package com.example.socialnetwork.infrastructure.kafka.event.suggestion.impl;

import com.example.socialnetwork.infrastructure.kafka.event.suggestion.SuggestionEvent;
import com.example.socialnetwork.infrastructure.kafka.event.EventType;

public class FriendDeleteEvent extends SuggestionEvent {
  public FriendDeleteEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.FRIEND_DELETED);
  }
}