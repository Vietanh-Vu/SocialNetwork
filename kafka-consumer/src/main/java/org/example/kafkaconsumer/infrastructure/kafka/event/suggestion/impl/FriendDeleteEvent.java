package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;


import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.SuggestionEvent;

public class FriendDeleteEvent extends SuggestionEvent {
  public FriendDeleteEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.FRIEND_DELETED);
  }
}