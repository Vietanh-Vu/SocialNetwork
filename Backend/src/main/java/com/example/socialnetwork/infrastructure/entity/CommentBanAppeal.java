package com.example.socialnetwork.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "comment_ban_appeals")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentBanAppeal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "appeal_id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotNull
  @Lob
  @Column(name = "reason", nullable = false)
  private String reason;

  @ColumnDefault("'PENDING'")
  @Lob
  @Column(name = "status")
  private String status;

  @Lob
  @Column(name = "admin_response")
  private String adminResponse;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(name = "resolved_at")
  private Instant resolvedAt;
}
