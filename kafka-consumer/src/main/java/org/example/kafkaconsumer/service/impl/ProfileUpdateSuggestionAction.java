package org.example.kafkaconsumer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.Suggestion;
import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.infrastructure.repository.SuggestionRepository;
import org.example.kafkaconsumer.infrastructure.repository.UserRepository;
import org.example.kafkaconsumer.service.AbstractSuggestionAction;
import org.example.kafkaconsumer.share.enums.Gender;
import org.example.kafkaconsumer.share.enums.SuggestionEventType;
import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileUpdateSuggestionAction extends AbstractSuggestionAction {
    private final UserRepository userRepository;
    private final SuggestionRepository suggestionRepository;

    @Override
    public boolean supports(SuggestionEventDto event) {
        return event.getType() == SuggestionEventType.PROFILE_UPDATED;
    }

    @Override
    @Transactional
    public void handle(SuggestionEventDto event) {
        Optional<User> userOptional = userRepository.findById(event.getUserId());
        List<Suggestion> suggestions = suggestionRepository.getSuggestionsByUserId(event.getUserId());
        
        if (userOptional.isPresent()) {
            User user1 = userOptional.get();
            for (Suggestion suggestion : suggestions) {
                User user2 = suggestion.getUser();
                if (Objects.equals(event.getUserId(), user2.getId())) {
                    user2 = suggestion.getFriend();
                }

                suggestion.setPoint(this.calculateMatchingProfile(user1, user2) + this.calculateMutualFriendsScore(user1.getId(), user2.getId()));
                suggestionRepository.save(suggestion);
            }
        }
    }
}