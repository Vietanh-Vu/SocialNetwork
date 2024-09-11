package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.port.spi.TagDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.example.socialnetwork.infrastructure.specification.TagSpecification.withPostId;

@RequiredArgsConstructor
public class TagDatabaseAdapter implements TagDatabasePort {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagDomain createTag(TagDomain tagDomain) {
        Tag tag = tagRepository.save(tagMapper.domainToEntity(tagDomain));
        return tagMapper.entityToDomain(tag);
    }

    @Override
    public void deleteTag(Long tagId) {
        if (tagRepository.existsById(tagId)) {
            tagRepository.deleteById(tagId);
        }else {
            throw new NotFoundException("Tag not found");
        }
    }


    @Override
    public Page<TagDomain> getAllTags(int page, int pageSize, Sort sort, Long postId) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(postId);
        return tagRepository.findAll(spec, pageable).map(tagMapper::entityToDomain);
    }

    @Override
    public TagDomain findByTaggedByUserIdAndTaggedUserIdAndPostId(Long userIdTag, Long userIdTagged, Long postId) {
        Tag tag = tagRepository.findByTaggedByUserIdAndTaggedUserIdAndPostId(userIdTag, userIdTagged, postId).orElse(null);
        if (tag != null) {
            return tagMapper.entityToDomain(tag);
        }
        return null;
    }

    private Specification<Tag> getSpec(Long postId) {
        Specification<Tag> spec = Specification.where(null);
        if (postId != null) {
            spec = spec.and(withPostId(postId));
        }

        return spec;
    }
}
