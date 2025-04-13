package org.example.kafkaconsumer.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.kafkaconsumer.share.enums.ERelationship;

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

    @JoinColumn(name = "user_id")
    private Long userId;

    private Long friendId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "relation")
    @Enumerated(EnumType.STRING)
    private ERelationship relation;
}