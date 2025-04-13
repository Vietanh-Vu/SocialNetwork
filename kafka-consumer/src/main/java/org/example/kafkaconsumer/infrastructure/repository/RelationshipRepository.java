package org.example.kafkaconsumer.infrastructure.repository;

import org.example.kafkaconsumer.infrastructure.entity.Relationship;
import org.example.kafkaconsumer.infrastructure.entity.User;
import org.example.kafkaconsumer.share.enums.ERelationship;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    @Query(value = "SELECT r.friend_id\n" +
        "FROM relationships r\n" +
        "WHERE r.user_id = :userId AND r.relation = 'FRIEND'\n" +
        "UNION\n" +
        "SELECT r.user_id\n" +
        "FROM relationships r\n" +
        "WHERE r.friend_id = :userId AND r.relation = 'FRIEND'",
           nativeQuery = true)
    List<Long> getFriendIdsByUserIdAndRelation(@Param("userId") long userId, @Param("relation") ERelationship relation);

    @Query("SELECT r FROM Relationship r " +
        "WHERE (r.userId = :userId AND r.friendId = :friendId) " +
        "OR (r.userId = :friendId AND r.friendId = :userId)")
    Relationship findByUser_IdAndFriend_Id(@Param("userId") long userId, @Param("friendId") long friendId);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT u FROM User u " +
        "INNER JOIN Relationship r ON r.userId = u.id OR r.friendId = u.id " +
        "WHERE r.relation = :relation " +
        "AND (r.friendId = :userId OR r.userId = :userId) " +
        "AND u.id <> :userId " +
        "ORDER BY r.createdAt DESC ")
    List<User> getListUserWithRelation(@Param("userId") long userId, @Param("relation") ERelationship relation);
}
