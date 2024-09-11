package com.example.socialnetwork.application.request;

import com.example.socialnetwork.common.ValidationRegex;
import com.example.socialnetwork.common.constant.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid", regexp = ValidationRegex.EMAIL_REGEX)
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;
    private String bio;
    private String location;
    private String gender;
    private String work;
    private String education;
    private LocalDate dateOfBirth;
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Pattern(message = "Password should be valid", regexp = ValidationRegex.PASSWORD_REGEX)
    private String password;

}
