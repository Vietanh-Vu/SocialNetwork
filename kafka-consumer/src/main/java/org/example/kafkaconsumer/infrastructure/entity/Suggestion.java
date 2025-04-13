package org.example.kafkaconsumer.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.kafkaconsumer.share.enums.Status;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id")
    private User friend;

    @Column(name = "suggest_point")
    private int point;

    @Column(name = "mutual_friends")
    private int mutualFriends;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
