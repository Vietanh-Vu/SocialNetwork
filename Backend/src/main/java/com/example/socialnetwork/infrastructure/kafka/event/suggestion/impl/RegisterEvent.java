package com.example.socialnetwork.infrastructure.kafka.event.suggestion.impl;

import com.example.socialnetwork.infrastructure.kafka.event.EventType;
import com.example.socialnetwork.infrastructure.kafka.event.suggestion.SuggestionEvent;

public class RegisterEvent extends SuggestionEvent {
  public RegisterEvent(long userId) {
    super(userId, EventType.USER_REGISTERED);
  }
}
