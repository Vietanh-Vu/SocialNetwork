package com.example.socialnetwork.application.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long postId;
    @Builder.Default
    private Long parentCommentId = null;
    private String content;
    @Builder.Default
    private MultipartFile[] image = null;
}
