package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.SuggestionMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.Suggestion;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.SuggestionRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RelationshipDatabaseAdapter implements RelationshipDatabasePort {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipMapper relationshipMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SuggestionRepository suggestionRepository;
    private final SuggestionMapper suggestionMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<RelationshipDomain> find(long senderId, long receiverId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(senderId, receiverId);
        return Optional.ofNullable(relationshipMapper.toRelationshipDomain(relationship));    }

    @Override
    @Transactional
    public ERelationship getRelationship(long userId, long friendId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        if (relationship == null) {
            return null;
        }
        return relationship.getRelation();
    }

    @Override
    @Transactional
    public List<UserDomain> getListSendRequest(long userId) {
        return userMapper.toUserDomains(relationshipRepository.findByUser_IdAndRelation(userId, ERelationship.PENDING));
    }

    @Override
    @Transactional
    public List<UserDomain> getListReceiveRequest(long userId) {
        return userMapper.toUserDomains(relationshipRepository.findByFriend_IdAndRelation(userId, ERelationship.PENDING));
    }

    @Override
    @Transactional
    public void deleteRequest(long senderId, long receiverId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(senderId, receiverId);
        relationshipRepository.delete(relationship);
    }

    @Override
    @Transactional
    public void unblock(long userId, long blockId) {
        Relationship relationship = relationshipRepository.getRelationship(userId, blockId);
        relationshipRepository.delete(relationship);
    }

    @Override
    @Transactional
    public List<UserDomain> getListFriend(long userId) {
        return userMapper.toUserDomains(relationshipRepository.getListUserWithRelation(userId, ERelationship.FRIEND));
    }

    @Override
    public List<UserDomain> getListBlock(long userId) {
        return userMapper.toUserDomains(relationshipRepository.getListBlock(userId));
    }

    @Override
    @Transactional
    public List<UserDomain> findFriendByKeyWord(long userId, String keyWord) {
        return userMapper.toUserDomains(relationshipRepository.getListFriendByKeyWord(userId, keyWord));
    }

    @Override
    @Transactional
    public List<UserDomain> getListSuggestionUser(long userId) {
        List<Suggestion> suggestions = suggestionRepository.findByUserOrFriend(userId);
        List<SuggestionDomain> suggestionDomains = suggestions.stream().map(suggestionMapper::toSuggestionDomain).toList();
        List<UserDomain> userDomains = new ArrayList<>();
        for (SuggestionDomain suggestionDomain : suggestionDomains) {
            if(suggestionDomain.getUser().getId()==userId) {
                userDomains.add(suggestionDomain.getFriend());
            }else {
                userDomains.add(suggestionDomain.getUser());
            }
        }
        return userDomains;
    }

    @Override
    @Transactional
    public void deleteFriend(long userId, long friendId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        if(relationship == null || relationship.getRelation() == ERelationship.PENDING) {
            throw new NotFoundException("you two are not friends");
        }else if(relationship.getRelation() == ERelationship.BLOCK) {
            throw new NotFoundException("You have been blocked by this person");
        }else {
            relationshipRepository.delete(relationship);
        }
    }

    @Override
    @Transactional
    public List<UserDomain> searchUserByKeyWord(long userId, String keyWord) {
//        User user1 = userRepository.findById(userId).orElseThrow();
        List<Suggestion> searchUsers = suggestionRepository.searchUser(userId);
        List<Suggestion> unsuitableSearchUsers = new ArrayList<>();
        for(Suggestion suggestion : searchUsers) {
            User user2 = suggestion.getUser();
            if(userId == user2.getId()) user2 = suggestion.getFriend();
            if (!user2.getEmail().toLowerCase().contains(keyWord.toLowerCase()) || !user2.getUsername().toLowerCase().contains(keyWord.toLowerCase())) {
                unsuitableSearchUsers.add(suggestion);
            }
        }
        searchUsers.removeAll(unsuitableSearchUsers);
        List<SuggestionDomain> suggestionDomains = searchUsers.stream().map(suggestionMapper::toSuggestionDomain).collect(Collectors.toList());
        List<UserDomain> userDomains = new ArrayList<>();
        for (SuggestionDomain suggestionDomain : suggestionDomains) {
            if(suggestionDomain.getUser().getId()==userId) {
                userDomains.add(suggestionDomain.getFriend());
            }else {
                userDomains.add(suggestionDomain.getUser());
            }
        }
        return userDomains;
    }


    @Override
    @Transactional
    public void updateRelation(long userId, long friendId, ERelationship eRelationship) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        if(relationship == null) {
            throw new NotFoundException("Not found relationship");
        }
        relationship.setRelation(eRelationship);
        relationshipRepository.save(relationship);
    }

    @Override
    @Transactional
    public void createRelationship(long userId, long friendId, ERelationship relation) {
        Relationship relationship = new Relationship();
        relationship.setUser(userRepository.findUserById(userId).get());
        relationship.setFriend(userRepository.findUserById(friendId).get());
        relationship.setRelation(relation);
        relationship.setCreatedAt(Instant.now());
        relationshipRepository.save(relationship);
    }
}
