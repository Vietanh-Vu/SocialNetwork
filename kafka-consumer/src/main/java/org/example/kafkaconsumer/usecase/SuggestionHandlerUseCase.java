package org.example.kafkaconsumer.usecase;

import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;

public interface SuggestionHandlerUseCase {
    void handle(SuggestionEventDto event);
}