package org.example.kafkaconsumer.infrastructure.kafka;

import org.example.kafkaconsumer.infrastructure.kafka.event.Event;

public interface EventHandler<T extends Event> {
    void handle(T event);
    Class<T> getEventType();
}