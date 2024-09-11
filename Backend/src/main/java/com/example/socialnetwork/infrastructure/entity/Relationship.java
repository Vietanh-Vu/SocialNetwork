package com.example.socialnetwork.infrastructure.entity;

import com.example.socialnetwork.common.constant.ERelationship;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "relationships")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id")
    private User friend;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "relation")
    @Enumerated(EnumType.STRING)
    private ERelationship relation;
}