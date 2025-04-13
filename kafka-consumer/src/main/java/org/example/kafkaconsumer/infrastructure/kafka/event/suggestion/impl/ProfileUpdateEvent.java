package org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.impl;


import org.example.kafkaconsumer.infrastructure.kafka.event.EventType;
import org.example.kafkaconsumer.infrastructure.kafka.event.suggestion.SuggestionEvent;

public class ProfileUpdateEvent extends SuggestionEvent {
  public ProfileUpdateEvent(long userId) {
    super(userId, EventType.PROFILE_UPDATED);
  }
}
