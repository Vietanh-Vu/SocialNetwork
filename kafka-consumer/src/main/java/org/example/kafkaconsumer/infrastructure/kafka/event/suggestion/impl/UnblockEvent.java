package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;


import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.UserInteractionEvent;

public class UnblockEvent extends UserInteractionEvent {
  public UnblockEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.USER_UNBLOCKED);
  }
}
