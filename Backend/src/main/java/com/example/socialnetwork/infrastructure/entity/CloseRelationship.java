package com.example.socialnetwork.infrastructure.entity;

import com.example.socialnetwork.common.constant.ECloseRelationship;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@FieldNameConstants
@Getter
@Setter
@Entity
@Table(name = "close_relationships", schema = "socialnetwork")
public class CloseRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "close_relationship_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @Lob
    @Enumerated(EnumType.STRING)
    @Column(name = "close_relationship_name")
    private ECloseRelationship closeRelationshipName;

    @Column(name = "created_at")
    private Instant createdAt;

}