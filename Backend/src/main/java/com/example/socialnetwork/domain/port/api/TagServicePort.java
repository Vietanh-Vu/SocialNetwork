package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.TagDomain;
import org.springframework.data.domain.Page;

public interface TagServicePort {
    TagDomain createTag(TagDomain tag);

    void deleteTag(Long tagId);

    Page<TagDomain> getAllTags(int page, int pageSize, String sortBy, String sortDirection, Long postId);

}
