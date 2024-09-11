package com.example.socialnetwork.application.response;

import com.example.socialnetwork.common.constant.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.Instant;

@Data
@Builder
public class ProfileResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String visibility;
    private String bio;
    private String location;
    private String work;
    private String education;
    private Instant createdAt;
    private Instant updatedAt;
    private String avatar;
    private String backgroundImage;
    private LocalDate dateOfBirth;
}