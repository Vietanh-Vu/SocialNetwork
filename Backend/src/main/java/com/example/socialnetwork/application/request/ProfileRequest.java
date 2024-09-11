package com.example.socialnetwork.application.request;

import com.example.socialnetwork.common.ValidationRegex;
import com.example.socialnetwork.common.constant.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;
    private String gender;
    private String visibility;
    private String bio;
    private String location;
    private String work;
    private String education;
    private LocalDate dateOfBirth;

    private MultipartFile[] avatar;

    private MultipartFile[] background;

}
