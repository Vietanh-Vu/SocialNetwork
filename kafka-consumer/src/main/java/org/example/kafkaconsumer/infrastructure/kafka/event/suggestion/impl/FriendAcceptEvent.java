package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;

import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.SuggestionEvent;

public class FriendAcceptEvent extends SuggestionEvent {
  public FriendAcceptEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.FRIEND_REQUEST_ACCEPTED);
  }
}