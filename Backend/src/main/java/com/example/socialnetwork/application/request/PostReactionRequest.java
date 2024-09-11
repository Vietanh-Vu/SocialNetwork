package com.example.socialnetwork.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReactionRequest {
    @NotNull(message = "Post ID is required")
    private Long postId;
    @NotBlank(message = "Reaction type is required")
    private String reactionType;
}
