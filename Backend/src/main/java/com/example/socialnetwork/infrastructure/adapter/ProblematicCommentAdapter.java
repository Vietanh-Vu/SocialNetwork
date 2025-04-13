package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.ProblematicCommentMapper;
import com.example.socialnetwork.domain.model.ProblematicCommentDomain;
import com.example.socialnetwork.domain.port.spi.ProblematicCommentPort;
import com.example.socialnetwork.infrastructure.entity.ProblematicComment;
import com.example.socialnetwork.infrastructure.repository.ProblematicCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProblematicCommentAdapter implements ProblematicCommentPort {
    private final ProblematicCommentRepository problematicCommentRepository;
    private final ProblematicCommentMapper problematicCommentMapper;

    @Override
    public void createProblematicComment(ProblematicCommentDomain comment) {
        ProblematicComment problematicComment = problematicCommentMapper.problematicCommentDomainToProblematicCommentEntity(comment);
        problematicCommentMapper.problematicCommentEntityToProblematicCommentDomain(problematicCommentRepository.save(problematicComment));
    }
}
