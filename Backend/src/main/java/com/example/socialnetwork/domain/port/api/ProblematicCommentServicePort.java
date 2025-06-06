package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.response.*;
import com.example.socialnetwork.domain.model.ProblematicCommentDomain;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface ProblematicCommentServicePort {
  Page<ProblematicCommentDomain> getFilteredProblematicComments(
      Double minProbability, Double maxProbability,
      Instant startDate, Instant endDate,
      int page, int pageSize, String sortBy, String sortDirection);

  ByteArrayInputStream exportToExcel(Double minProbability, Double maxProbability) throws IOException;

  DashboardStatResponse getDashboardStats();

  WeeklyCommentResponse getWeeklyCommentCounts();

  MonthlyCommentResponse getMonthlyCommentCounts(Instant startDate, Instant endDate);

  TopViolatingUsersResponse getTopViolatingUsers(int limit);

  Page<ProblematicCommentDomain> getUserProblematicComments(Long userId, int page, int pageSize, String sortBy, String sortDirection);

  TopViolatingUsersResponse getTopViolatingUsers(int limit, boolean includeBanned, boolean onlyBanned);

  UserWeeklyViolationStatsResponse getUserWeeklyViolationStats(Long userId);
}
