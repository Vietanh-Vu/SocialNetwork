package org.example.kafkaconsumer.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.service.ISuggestionAction;
import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;
import org.example.kafkaconsumer.usecase.SuggestionHandlerUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionHandlerUseCaseImpl implements SuggestionHandlerUseCase {
    private final List<ISuggestionAction> suggestionActions;

    @Override
    public void handle(SuggestionEventDto event) {
        suggestionActions.stream()
            .filter(action -> action.supports(event))
            .forEach(action -> action.handle(event));
    }
}