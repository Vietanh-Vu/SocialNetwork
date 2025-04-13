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
                
                int addition = calculateMutualFriendsScore(suggestion.getMutualFriends());
                suggestion.setPoint(addition + calculateScore(user1, user2));
                suggestionRepository.save(suggestion);
            }
        }
    }

    private int calculateMutualFriendsScore(int mutualFriends) {
        if (mutualFriends > 0 && mutualFriends < 11) return 10;
        if (mutualFriends > 10 && mutualFriends < 21) return 20;
        if (mutualFriends > 20) return 30;
        return 0;
    }

    private int calculateScore(User user1, User user2) {
        int score = 0;
        if (Objects.equals(user1.getLocation(), user2.getLocation())) score += 10;
        if (user1.getGender() == Gender.FEMALE && user2.getGender() == Gender.MALE) score += 10;
        if (user1.getGender() == Gender.MALE && user2.getGender() == Gender.FEMALE) score += 10;
        if (user1.getGender() == Gender.OTHERS && user2.getGender() == Gender.OTHERS) score += 10;
        if (user1.getDateOfBirth().equals(user2.getDateOfBirth())) score += 10;
        return score;
    }
}