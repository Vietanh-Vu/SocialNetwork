package com.example.socialnetwork.application.request;


import com.example.socialnetwork.common.constant.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private Long id = null;

    private String content;

    private Visibility visibility;

    private MultipartFile[] photoLists;

    private String tagUsers;


}
