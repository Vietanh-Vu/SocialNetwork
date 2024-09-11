package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long>{
    @Query("SELECT r FROM Suggestion r " +
            "WHERE (r.user.id = :userId OR r.friend.id = :userId) " +
            "AND r.status = 'NONE' " +
            "ORDER BY r.point DESC, r.mutualFriends DESC ")
    List<Suggestion> findByUserOrFriend(@Param("userId") long userId);

    @Query("SELECT r FROM Suggestion r " +
            "WHERE (r.user.id = :userId OR r.friend.id = :userId) ")
    List<Suggestion> getSuggestionsByUserId(@Param("userId") long userId);

    @Query("SELECT r FROM Suggestion r " +
            "WHERE (r.user.id = :user1Id AND r.friend.id = :user2Id) " +
             "OR (r.user.id = :user2Id AND r.friend.id = :user1Id)")
    Suggestion findByUserAndFriend(@Param("user1Id") long user1Id, @Param("user2Id") long user2Id);

    @Query("SELECT r FROM Suggestion r " +
            "WHERE (r.user.id = :userId OR r.friend.id = :userId) " +
            "AND r.status <> 'BLOCK' " +
            "ORDER BY r.status ASC, r.point DESC, r.mutualFriends DESC ")
    List<Suggestion> searchUser(@Param("userId") long userId);
}
