package com.example.socialnetwork.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "global_configs")
public class GlobalConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 255)
  @NotNull
  @ColumnDefault("''")
  @Column(name = "name", nullable = false)
  private String name;

  @Size(max = 50)
  @ColumnDefault("''")
  @Column(name = "code", length = 50)
  private String code;

  @Lob
  @Column(name = "`desc`")
  private String desc;

  @Column(name = "created", nullable = false)
  private Instant created;

}