package com.example.socialnetwork.infrastructure.repository;


import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface RelationshipRepository extends JpaRepository<Relationship, Long>, PagingAndSortingRepository<Relationship, Long> {
    @Query("SELECT r FROM Relationship r " +
            "WHERE (r.user.id = :userId AND r.friend.id = :friendId) " +
            "OR (r.user.id = :friendId AND r.friend.id = :userId)")
    Relationship findByUser_IdAndFriend_Id(@Param("userId") long userId, @Param("friendId") long friend_id);

    @Query("SELECT r.user FROM Relationship r " +
            "WHERE r.relation = :relation " +
            "AND r.friend.id = :userId " +
            "ORDER BY r.createdAt DESC ")
    List<User> findByFriend_IdAndRelation(@Param("userId") long userId, @Param("relation") ERelationship relation);

    @Query("SELECT r.friend FROM Relationship r " +
            "WHERE r.relation = :relation " +
            "AND r.user.id = :userId " +
            "ORDER BY r.createdAt DESC ")
    List<User> findByUser_IdAndRelation(@Param("userId") long userId, @Param("relation") ERelationship relation);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT u FROM User u " +
            "INNER JOIN Relationship r ON r.user.id = u.id OR r.friend.id = u.id " +
            "WHERE r.relation = :relation " +
            "AND (r.friend.id = :userId OR r.user.id = :userId) " +
            "AND u.id <> :userId")
    Page<User> getListUserWithRelation(@Param("userId") long userId, @Param("relation") ERelationship relation, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT u FROM User u " +
            "INNER JOIN Relationship r ON r.user.id = u.id OR r.friend.id = u.id " +
            "WHERE r.relation = :relation " +
            "AND (r.friend.id = :userId OR r.user.id = :userId) " +
            "AND u.id <> :userId " +
            "ORDER BY r.createdAt DESC ")
    List<User> getListUserWithRelation(@Param("userId") long userId, @Param("relation") ERelationship relation);


    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT r.friend FROM Relationship r " +
            "WHERE r.relation = 'BLOCK' " +
            "AND r.user.id = :userId " +
            "ORDER BY r.createdAt DESC ")
    List<User> getListBlock(@Param("userId") long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT u FROM User u " +
            "INNER JOIN Relationship r ON r.user.id = u.id OR r.friend.id = u.id " +
            "WHERE r.relation = 'FRIEND' " +
            "AND (r.friend.id = :userId OR r.user.id = :userId) " +
            "AND u.id <> :userId " +
            "AND (u.email LIKE %:keyWord% OR u.username LIKE %:keyWord%)")
    Page<User> getListFriendByKeyWord(@Param("userId") long userId, @Param("keyWord") String keyWord, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT u FROM User u " +
            "INNER JOIN Relationship r ON r.user.id = u.id OR r.friend.id = u.id " +
            "WHERE r.relation = 'FRIEND' " +
            "AND (r.friend.id = :userId OR r.user.id = :userId) " +
            "AND u.id <> :userId " +
            "AND (u.email LIKE %:keyWord% OR u.username LIKE %:keyWord%)")
    List<User> getListFriendByKeyWord(@Param("userId") long userId, @Param("keyWord") String keyWord);

    @Query("SELECT r FROM Relationship r " +
            "WHERE (r.user.id = :userId AND r.friend.id = :friendId) ")
    Relationship getRelationship(@Param("userId") long userId, @Param("friendId") long friendId);
}
