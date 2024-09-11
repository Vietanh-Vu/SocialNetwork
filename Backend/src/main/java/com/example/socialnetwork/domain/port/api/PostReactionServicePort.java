package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.domain.model.PostReactionDomain;
import org.springframework.data.domain.Page;

public interface PostReactionServicePort {
    PostReactionDomain createPostReaction(PostReactionDomain postReactionDomain);
    Boolean deletePostReaction(Long postReactionId);
    PostReactionDomain getPostReaction(Long postReactionId);
    Page<PostReactionDomain> getAllPostReactions(int page, int pageSize, String sortBy, String sortDirection, Long postReactionId, String postReactionType);

}
