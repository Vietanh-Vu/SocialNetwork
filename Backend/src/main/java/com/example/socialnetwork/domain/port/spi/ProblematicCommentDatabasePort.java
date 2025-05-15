package com.example.socialnetwork.domain.port.spi;

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
    // Get problematic comments with pagination
    Page<ProblematicCommentDomain> getAllProblematicComments(Pageable pageable);

    // Get problematic comments filtered by spam probability
    Page<ProblematicCommentDomain> getProblematicCommentsByProbability(Double minProbability, Double maxProbability, Pageable pageable);

    // Get problematic comments filtered by date
    Page<ProblematicCommentDomain> getProblematicCommentsByDateRange(Instant startDate, Instant endDate, Pageable pageable);

    // Get problematic comments filtered by both probability and date
    Page<ProblematicCommentDomain> getProblematicCommentsByProbabilityAndDateRange(
        Double minProbability, Double maxProbability,
        Instant startDate, Instant endDate,
        Pageable pageable);

    void streamProblematicCommentsByProbabilityAndDateRange(
        Double minProbability, Double maxProbability,
        Instant startDate, Instant endDate,
        ResultSetExtractor<Void> resultSetExtractor);

    // Get count by date range (weekly/monthly)
    Long countByDateRange(Instant startDate, Instant endDate);

    // Get top violating users
    List<TopViolatingUserDomain> getTopViolatingUsers(int limit);
}
