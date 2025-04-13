package com.example.socialnetwork.infrastructure.kafka;

import com.example.socialnetwork.infrastructure.kafka.event.Event;

public interface EventHandler<T extends Event> {
    void handle(T event);
    Class<T> getEventType();
}