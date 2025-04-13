package org.example.kafkaconsumer.service;

import lombok.RequiredArgsConstructor;
import org.example.kafkaconsumer.infrastructure.entity.Suggestion;
import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.infrastructure.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractSuggestionAction implements ISuggestionAction {
    @Autowired
    protected SuggestionRepository suggestionRepository;

    protected void updatePoint(long user1Id, List<User> users, int point) {
        for (User user2 : users) {
            Suggestion suggestion = suggestionRepository.findByUserAndFriend(user1Id, user2.getId());
            if (suggestion == null) continue;
            int numberOfMutualFriends = suggestion.getMutualFriends() + point;
            suggestion.setMutualFriends(numberOfMutualFriends);
            if (point < 0) {
                if (numberOfMutualFriends == 0 || numberOfMutualFriends == 10 || numberOfMutualFriends == 20)
                    suggestion.setPoint(suggestion.getPoint() + point * 10);

            } else {
                if (numberOfMutualFriends == 1 || numberOfMutualFriends == 11 || numberOfMutualFriends == 21)
                    suggestion.setPoint(suggestion.getPoint() + point * 10);
            }
            System.out.println(suggestion.getPoint());
            suggestionRepository.save(suggestion);
        }
    }
}