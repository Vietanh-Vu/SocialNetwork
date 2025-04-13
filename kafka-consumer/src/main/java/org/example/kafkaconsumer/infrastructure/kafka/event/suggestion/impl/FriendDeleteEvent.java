package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;

import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.UserInteractionEvent;

public class FriendDeleteEvent extends UserInteractionEvent {
  public FriendDeleteEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.FRIEND_DELETED);
  }
}
