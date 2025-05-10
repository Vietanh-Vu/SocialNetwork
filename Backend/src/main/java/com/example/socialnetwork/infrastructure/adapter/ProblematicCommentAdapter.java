package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.ProblematicCommentMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.ProblematicCommentDomain;
import com.example.socialnetwork.domain.model.TopViolatingUserDomain;
import com.example.socialnetwork.domain.port.spi.ProblematicCommentDatabasePort;
import com.example.socialnetwork.infrastructure.entity.ProblematicComment;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.ProblematicCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProblematicCommentAdapter implements ProblematicCommentDatabasePort {
    private final ProblematicCommentRepository problematicCommentRepository;
    private final ProblematicCommentMapper problematicCommentMapper;
    private final UserMapper userMapper;

    @Override
    public void createProblematicComment(ProblematicCommentDomain comment) {
        ProblematicComment problematicComment = problematicCommentMapper.problematicCommentDomainToProblematicCommentEntity(comment);
        problematicCommentMapper.problematicCommentEntityToProblematicCommentDomain(problematicCommentRepository.save(problematicComment));
    }

    @Override
    public Page<ProblematicCommentDomain> getAllProblematicComments(Pageable pageable) {
        return problematicCommentRepository.findAll(pageable)
            .map(problematicCommentMapper::problematicCommentEntityToProblematicCommentDomain);
    }

    @Override
    public Page<ProblematicCommentDomain> getProblematicCommentsByProbability(Double minProbability, Double maxProbability, Pageable pageable) {
        return problematicCommentRepository.findBySpamProbabilityBetween(minProbability, maxProbability, pageable)
            .map(problematicCommentMapper::problematicCommentEntityToProblematicCommentDomain);
    }

    @Override
    public Page<ProblematicCommentDomain> getProblematicCommentsByDateRange(Instant startDate, Instant endDate, Pageable pageable) {
        return problematicCommentRepository.findByCreatedAtBetween(startDate, endDate, pageable)
            .map(problematicCommentMapper::problematicCommentEntityToProblematicCommentDomain);
    }

    @Override
    public Page<ProblematicCommentDomain> getProblematicCommentsByProbabilityAndDateRange(
        Double minProbability, Double maxProbability,
        Instant startDate, Instant endDate,
        Pageable pageable) {
        return problematicCommentRepository.findBySpamProbabilityBetweenAndCreatedAtBetween(
                minProbability, maxProbability, startDate, endDate, pageable)
            .map(problematicCommentMapper::problematicCommentEntityToProblematicCommentDomain);
    }

    @Override
    public Long countByDateRange(Instant startDate, Instant endDate) {
        return problematicCommentRepository.countByDateRange(startDate, endDate);
    }

    @Override
    public List<TopViolatingUserDomain> getTopViolatingUsers(int limit) {
        List<Object[]> topUsers = problematicCommentRepository.findTopViolatingUsers(PageRequest.of(0, limit));

        return topUsers.stream().map(result -> {
            User user = (User) result[0];
            Long count = (Long) result[1];

            return TopViolatingUserDomain.builder()
                .user(userMapper.toUserDomain(user))
                .commentCount(count)
                .build();
        }).collect(Collectors.toList());
    }
}
