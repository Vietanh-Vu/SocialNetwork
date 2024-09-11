package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.response.FriendResponse;
import org.springframework.data.domain.Page;

public interface RelationshipServicePort {
    void deleteRelationship(long friendId);

    void sendRequestMakeFriendship(long userId);

    void deleteRequestMakeFriendship(long userId);

    void acceptRequestMakeFriendship(long userId);

    void refuseRequestMakeFriendship(long userId);

    void block(long friendId);

    void unblock(long friendId);

    int getNumberOfFriend();

    Page<FriendResponse> findFriend(int page, int pageSize, String keyWord);

    Page<FriendResponse> getListReceiveRequest(int page, int pageSize);

    Page<FriendResponse> getListSendRequest(int page, int pageSize);

    Page<FriendResponse> getListFriend(int page, int pageSize, String sortDirection, String sortBy);

    Page<FriendResponse> getListBlock(int page, int pageSize, String sortDirection, String sortBy);

    Page<FriendResponse> getFriendSuggestions(int page, int pageSize);

    Page<FriendResponse> searchUser(int page, int pageSize, String keyWord);
}
