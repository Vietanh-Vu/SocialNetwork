package com.example.socialnetwork.infrastructure.kafka.event.suggestion.impl;

import com.example.socialnetwork.infrastructure.kafka.event.EventType;
import com.example.socialnetwork.infrastructure.kafka.event.suggestion.SuggestionEvent;

public class ProfileUpdateEvent extends SuggestionEvent {
  public ProfileUpdateEvent(long userId) {
    super(userId, EventType.PROFILE_UPDATED);
  }
}
