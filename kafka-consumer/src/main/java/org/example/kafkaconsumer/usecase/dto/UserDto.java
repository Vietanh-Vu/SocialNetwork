package org.example.kafkaconsumer.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.kafkaconsumer.share.enums.Gender;
import org.example.kafkaconsumer.share.enums.Visibility;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private Long userId;
  private String username;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Visibility visibility;
  private Long roleId;
  private String bio;
  private String location;
  private String work;
  private String education;
  private Instant createdAt;
  private Instant updatedAt;
  private String avatar;
  private String backgroundImage;
  private LocalDate dateOfBirth;
  private Boolean isEmailVerified;
  private List<Long> friendIds;
}
