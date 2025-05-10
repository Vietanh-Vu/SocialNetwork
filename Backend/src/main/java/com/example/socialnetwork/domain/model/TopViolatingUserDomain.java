package com.example.socialnetwork.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopViolatingUserDomain {
  private UserDomain user;
  private Long commentCount;
}