package org.example.kafkaconsumer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.Suggestion;
import org.example.kafkaconsumer.infrastructure.repository.SuggestionRepository;
import org.example.kafkaconsumer.service.AbstractSuggestionAction;
import org.example.kafkaconsumer.service.ISuggestionAction;
import org.example.kafkaconsumer.share.enums.Status;
import org.example.kafkaconsumer.share.enums.SuggestionEventType;
import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnblockSuggestionAction extends AbstractSuggestionAction {
    private final SuggestionRepository suggestionRepository;

    @Override
    public boolean supports(SuggestionEventDto event) {
        return event.getType() == SuggestionEventType.USER_UNBLOCKED;
    }

    @Override
    @Transactional
    public void handle(SuggestionEventDto event) {
        Suggestion suggestion = suggestionRepository.findByUserAndFriend(event.getUserId(), event.getTargetUserId());
        suggestion.setStatus(Status.NONE);
        suggestionRepository.save(suggestion);
        this.updateUserDocument(List.of(event.getUserId(), event.getTargetUserId()));
    }
}