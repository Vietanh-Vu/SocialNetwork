package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostDatabasePort {
    PostDomain createPost(PostDomain postDomain);
    PostDomain updatePost(PostDomain postDomain);
    void deletePost(Long postId);
    PostDomain findById(Long id);
    Page<PostDomain> getAllPosts(int page, int pageSize, Sort sort, Long targetUserId, List<Visibility> visibility);
    Page<PostDomain> getAllPostByFriends(Pageable pageable, List<Long> targetUserIds, List<Visibility> visibility);
    Long countPostByUserId(Long userId);
}
