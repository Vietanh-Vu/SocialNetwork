package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;


import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.UserInteractionEvent;

public class FriendAcceptEvent extends UserInteractionEvent {
  public FriendAcceptEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.FRIEND_REQUEST_ACCEPTED);
  }
}
