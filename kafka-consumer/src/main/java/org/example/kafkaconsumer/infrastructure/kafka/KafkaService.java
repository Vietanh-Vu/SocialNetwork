package org.example.kafkaconsumer.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.kafkaconsumer.infrastructure.kafka.event.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<Class<? extends Event>, EventHandler<? extends Event>> handlers;

    public KafkaService(KafkaTemplate<String, Object> kafkaTemplate, List<EventHandler<? extends Event>> eventHandlers) {
        this.kafkaTemplate = kafkaTemplate;
        this.handlers = new HashMap<>();
        eventHandlers.forEach(handler -> handlers.put(handler.getEventType(), handler));
    }

    public <T extends Event> void publish(T event) {
        log.info("Publishing event: {} to topic: {}", event.getType(), event.getTopic());
        kafkaTemplate.send(event.getTopic(), event);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void handle(T event) {
        log.info("Handling event: {}", event.getType());
        EventHandler<T> handler = (EventHandler<T>) handlers.get(event.getClass());
        if (handler != null) {
            handler.handle(event);
        } else {
            log.warn("No handler found for event type: {}", event.getType());
        }
    }
}
