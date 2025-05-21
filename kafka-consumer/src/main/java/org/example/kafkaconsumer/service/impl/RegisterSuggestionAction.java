package org.example.kafkaconsumer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.Suggestion;
import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.infrastructure.repository.SuggestionRepository;
import org.example.kafkaconsumer.infrastructure.repository.UserRepository;
import org.example.kafkaconsumer.service.AbstractSuggestionAction;
import org.example.kafkaconsumer.share.enums.Gender;
import org.example.kafkaconsumer.share.enums.Status;
import org.example.kafkaconsumer.share.enums.SuggestionEventType;
import org.example.kafkaconsumer.consumer.dto.SuggestionEventDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegisterSuggestionAction extends AbstractSuggestionAction {
    private final UserRepository userRepository;
    private final SuggestionRepository suggestionRepository;

    @Override
    public boolean supports(SuggestionEventDto event) {
        return event.getType() == SuggestionEventType.USER_REGISTERED;
    }

    @Override
    @Transactional
    public void handle(SuggestionEventDto event) {
        Optional<User> userOptional = userRepository.findById(event.getUserId());
        List<User> users = userRepository.findAll();

        // tính toán điểm của user mới so với toàn bộ user trong hệ thống
        if (userOptional.isPresent() && !users.isEmpty()) {
            User user1 = userOptional.get();
            for (User user2 : users) {
                if (Objects.equals(user2.getId(), user1.getId()) || !user2.getIsEmailVerified()) {
                    continue;
                }
                
                Suggestion suggestion = Suggestion.builder()
                    .user(user1)
                    .friend(user2)
                    .point(calculateMatchingProfile(user1, user2))
                    .status(Status.NONE)
                    .build();
                    
                suggestionRepository.save(suggestion);
            }
        }
    }
}