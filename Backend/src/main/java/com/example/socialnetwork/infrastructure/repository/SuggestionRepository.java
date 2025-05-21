package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long>{
    @Query("SELECT s FROM Suggestion s " +
            "WHERE (s.user.id = :userId OR s.friend.id = :userId) " +
            "AND s.status = 'NONE' " +
            "ORDER BY s.point DESC")
    List<Suggestion> findByUserOrFriend(@Param("userId") long userId);

    @Query("SELECT s FROM Suggestion s " +
            "WHERE (s.user.id = :userId OR s.friend.id = :userId) ")
    List<Suggestion> getSuggestionsByUserId(@Param("userId") long userId);

    @Query("SELECT s FROM Suggestion s " +
            "WHERE (s.user.id = :user1Id AND s.friend.id = :user2Id) " +
             "OR (s.user.id = :user2Id AND s.friend.id = :user1Id)")
    Suggestion findByUserAndFriend(@Param("user1Id") long user1Id, @Param("user2Id") long user2Id);

    @Query("SELECT s FROM Suggestion s " +
            "WHERE (s.user.id = :userId OR s.friend.id = :userId) " +
            "AND s.status <> 'BLOCK' " +
            "ORDER BY s.status ASC, s.point DESC")
    List<Suggestion> searchUser(@Param("userId") long userId);

    List<Suggestion> findAllByUser_IdAndFriend_IdIn(Long userId, List<Long> friendId);
}
