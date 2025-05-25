package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;


import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.SuggestionEvent;

public class UnblockEvent extends SuggestionEvent {
  public UnblockEvent(long userId, long targetUserId) {
    super(userId, targetUserId, EventType.USER_UNBLOCKED);
  }
}