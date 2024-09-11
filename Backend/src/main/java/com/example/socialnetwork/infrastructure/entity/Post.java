package com.example.socialnetwork.infrastructure.entity;

import com.example.socialnetwork.common.constant.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@FieldNameConstants
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 255)
    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "visibility")
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "last_comment")
    private Instant lastComment;

    @Column(name = "photo_lists", columnDefinition = "MEDIUMTEXT")
    private String photoLists;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostReaction> postReactions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "post", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

}