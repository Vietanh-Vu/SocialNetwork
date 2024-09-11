package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface RelationshipDatabasePort {

    Optional<RelationshipDomain> find(long senderId, long receiverId);

    ERelationship getRelationship(long senderId, long receiverId);

    List<UserDomain> getListSendRequest(long userId);

    List<UserDomain> getListReceiveRequest(long userId);

    List<UserDomain> getListFriend(long userId);

    List<UserDomain> getListBlock(long userId);

    List<UserDomain> findFriendByKeyWord(long userId, String keyWord);

    List<UserDomain> getListSuggestionUser(long userId);

    List<UserDomain> searchUserByKeyWord(long userId, String keyWord);

    void deleteFriend(long userId, long friendId);

    void deleteRequest(long senderId, long receiverId);

    void unblock(long userId, long blockId);

    void updateRelation(long senderId, long receiverId, ERelationship relationship);

    void createRelationship(long senderId, long receiverId, ERelationship relation);
}
