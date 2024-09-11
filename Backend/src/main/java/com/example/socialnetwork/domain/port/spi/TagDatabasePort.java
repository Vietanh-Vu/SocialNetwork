package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.TagDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TagDatabasePort {
    TagDomain createTag(TagDomain tagDomain);

    void deleteTag(Long tagId);

    Page<TagDomain> getAllTags(int page, int pageSize, Sort sort, Long postId);

    TagDomain findByTaggedByUserIdAndTaggedUserIdAndPostId(Long userIdTag, Long userIdTagged, Long postId);
}
