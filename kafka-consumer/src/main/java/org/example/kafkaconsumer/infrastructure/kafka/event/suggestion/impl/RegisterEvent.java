package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;

import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.SuggestionEvent;

public class RegisterEvent extends SuggestionEvent {
  public RegisterEvent(long userId) {
    super(userId, EventType.USER_REGISTERED);
  }
}
