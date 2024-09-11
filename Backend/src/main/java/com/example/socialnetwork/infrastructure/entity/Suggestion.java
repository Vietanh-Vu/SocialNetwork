package com.example.socialnetwork.infrastructure.entity;

import com.example.socialnetwork.common.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suggestions")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suggestion_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private User friend;

    @Column(name = "suggest_point")
    private int point;

    @Column(name = "mutual_friends")
    private int mutualFriends;

    @ColumnDefault("NONE")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
