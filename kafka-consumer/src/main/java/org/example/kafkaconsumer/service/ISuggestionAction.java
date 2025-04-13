package org.example.kafkaconsumer.service;

import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;

public interface ISuggestionAction {
    boolean supports(SuggestionEventDto event);
    void handle(SuggestionEventDto event);
}