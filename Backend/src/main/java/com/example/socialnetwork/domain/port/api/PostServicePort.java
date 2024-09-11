package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.request.PostUpdate;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.model.PostDomain;
import org.springframework.data.domain.Page;

public interface PostServicePort {
    PostDomain createPost(PostDomain postDomain);
    PostDomain updatePost(PostUpdate postUpdate);
    void deletePost(Long postId);
    Page<PostResponse> getAllPosts(int page, int pageSize, String sortBy, String sortDirection, Long userId, Long targetUserId);

    Page<PostResponse> getNewsFeed(int page, int pageSize, long userId);

    Long countPostByUserId();
}
