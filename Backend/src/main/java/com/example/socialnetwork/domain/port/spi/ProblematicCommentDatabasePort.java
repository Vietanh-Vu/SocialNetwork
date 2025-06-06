package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.application.response.MonthlyViolationData;
import com.example.socialnetwork.application.response.WeeklyViolationData;
import com.example.socialnetwork.domain.model.ProblematicCommentDomain;
import com.example.socialnetwork.domain.model.TopViolatingUserDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface ProblematicCommentDatabasePort {
    void createProblematicComment(ProblematicCommentDomain comment);
    Page<ProblematicCommentDomain> getAllProblematicComments(Pageable pageable);

    Page<ProblematicCommentDomain> getProblematicCommentsByProbability(Double minProbability, Double maxProbability, Pageable pageable);

    Page<ProblematicCommentDomain> getProblematicCommentsByDateRange(Instant startDate, Instant endDate, Pageable pageable);

    Page<ProblematicCommentDomain> getProblematicCommentsByProbabilityAndDateRange(
        Double minProbability, Double maxProbability,
        Instant startDate, Instant endDate,
        Pageable pageable);

    void streamProblematicCommentsByProbabilityAndDateRange(
        Double minProbability, Double maxProbability,
        Instant startDate, Instant endDate,
        ResultSetExtractor<Void> resultSetExtractor);

    Long countByDateRange(Instant startDate, Instant endDate);

    List<TopViolatingUserDomain> getTopViolatingUsers(int limit);

    Page<ProblematicCommentDomain> getProblematicCommentsByUserId(Long userId, Pageable pageable);

    List<MonthlyViolationData> getUserMonthlyViolationStats(Long userId, Instant startDate, Instant endDate);

    Long countByUser(Long userId);

    List<WeeklyViolationData> getUserWeeklyViolationStats(Long userId, Instant startDate, Instant endDate);
}
