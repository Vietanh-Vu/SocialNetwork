package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.application.response.MonthlyViolationData;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createProblematicComment(ProblematicCommentDomain comment) {
        ProblematicComment problematicComment = problematicCommentMapper.problematicCommentDomainToProblematicCommentEntity(comment);
        problematicCommentMapper.problematicCommentEntityToProblematicCommentDomain(problematicCommentRepository.save(problematicComment));
    }

    @Override
    @Transactional(readOnly = true)
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
    public void streamProblematicCommentsByProbabilityAndDateRange(
        Double minProbability,
        Double maxProbability,
        Instant startDate,
        Instant endDate,
        ResultSetExtractor<Void> resultSetExtractor) {
        String sql = "SELECT *" +
            "FROM problematic_comments pc " +
            "WHERE pc.spam_probability BETWEEN ? AND ? " +
            "AND pc.created_at BETWEEN ? AND ? " +
            "ORDER BY pc.created_at ASC";

        jdbcTemplate.query(sql,
            new Object[]{minProbability, maxProbability, startDate, endDate},
            resultSetExtractor);
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

  @Override
  public Page<ProblematicCommentDomain> getProblematicCommentsByUserId(Long userId, Pageable pageable) {
    User user = User.builder().id(userId).build();
    return problematicCommentRepository.findByUser(user, pageable)
        .map(problematicCommentMapper::problematicCommentEntityToProblematicCommentDomain);
  }

  @Override
  public List<MonthlyViolationData> getUserMonthlyViolationStats(Long userId, Instant startDate, Instant endDate) {
    String sql = "SELECT YEAR(pc.created_at) as year, MONTH(pc.created_at) as month, COUNT(*) as count " +
        "FROM problematic_comments pc " +
        "WHERE pc.user_id = ? " +
        "AND pc.created_at BETWEEN ? AND ? " +
        "GROUP BY YEAR(pc.created_at), MONTH(pc.created_at) " +
        "ORDER BY year, month";

    return jdbcTemplate.query(sql,
        (rs, rowNum) -> MonthlyViolationData.builder()
            .year(rs.getInt("year"))
            .month(rs.getInt("month"))
            .count(rs.getLong("count"))
            .build(),
        userId, startDate, endDate);
  }

  @Override
  public Long countByUser(Long userId) {
    User user = User.builder().id(userId).build();
    return problematicCommentRepository.countByUser(user);
  }
}
