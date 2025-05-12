package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.response.DashboardStatResponse;
import com.example.socialnetwork.application.response.MonthlyCommentResponse;
import com.example.socialnetwork.application.response.TopViolatingUsersResponse;
import com.example.socialnetwork.application.response.WeeklyCommentResponse;
import com.example.socialnetwork.domain.model.ProblematicCommentDomain;
import com.example.socialnetwork.domain.model.TopViolatingUserDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.ProblematicCommentServicePort;
import com.example.socialnetwork.domain.port.spi.ProblematicCommentDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblematicCommentServiceImpl implements ProblematicCommentServicePort {
  private final ProblematicCommentDatabasePort problematicCommentPort;

  @Override
  public Page<ProblematicCommentDomain> getFilteredProblematicComments(
      Double minProbability, Double maxProbability,
      Instant startDate, Instant endDate,
      int page, int pageSize, String sortBy, String sortDirection) {

    // Create pageable object
    Sort sort = Sort.by(sortBy);
    if ("desc".equalsIgnoreCase(sortDirection)) {
      sort = sort.descending();
    } else {
      sort = sort.ascending();
    }
    Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

    // Apply filters based on what parameters are provided
    if (minProbability != null && maxProbability != null && startDate != null && endDate != null) {
      return problematicCommentPort.getProblematicCommentsByProbabilityAndDateRange(
          minProbability, maxProbability, startDate, endDate, pageable);
    } else if (minProbability != null && maxProbability != null) {
      return problematicCommentPort.getProblematicCommentsByProbability(minProbability, maxProbability, pageable);
    } else if (startDate != null && endDate != null) {
      return problematicCommentPort.getProblematicCommentsByDateRange(startDate, endDate, pageable);
    } else {
      return problematicCommentPort.getAllProblematicComments(pageable);
    }
  }

  @Override
  public ByteArrayInputStream exportToExcel(Double minProbability, Double maxProbability) throws IOException {
    Pageable pageable = PageRequest.of(0, 1000, Sort.by("createdAt").descending());
    Page<ProblematicCommentDomain> comments;

    if (minProbability != null && maxProbability != null) {
      comments = problematicCommentPort.getProblematicCommentsByProbability(minProbability, maxProbability, pageable);
    } else {
      comments = problematicCommentPort.getAllProblematicComments(pageable);
    }

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("Problematic Comments");

      // Header row
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("ID");
      headerRow.createCell(1).setCellValue("User ID");
      headerRow.createCell(2).setCellValue("Content");
      headerRow.createCell(3).setCellValue("Probability");
      headerRow.createCell(4).setCellValue("Created Date");

      // Data rows
      int rowIdx = 1;
      for (ProblematicCommentDomain comment : comments.getContent()) {
        Row row = sheet.createRow(rowIdx++);
        row.createCell(0).setCellValue(comment.getId());
        row.createCell(1).setCellValue(comment.getUser().getId());
        row.createCell(2).setCellValue(comment.getContent());
        row.createCell(3).setCellValue(comment.getSpamProbability());
        row.createCell(4).setCellValue(comment.getCreatedAt().toString());
      }

      // Resize columns
      for (int i = 0; i < 5; i++) {
        sheet.autoSizeColumn(i);
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    }
  }

  @Override
  public DashboardStatResponse getDashboardStats() {
    // Today's stats
    Instant startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
    Instant endOfToday = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
    Long todayCount = problematicCommentPort.countByDateRange(startOfToday, endOfToday);

    // This week's stats
    LocalDate today = LocalDate.now();
    LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
    Instant startOfWeekInstant = startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Long weeklyCount = problematicCommentPort.countByDateRange(startOfWeekInstant, endOfToday);

    // This month's stats
    LocalDate startOfMonth = today.withDayOfMonth(1);
    Instant startOfMonthInstant = startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Long monthlyCount = problematicCommentPort.countByDateRange(startOfMonthInstant, endOfToday);

    // All time count
    Long totalCount = problematicCommentPort.countByDateRange(Instant.EPOCH, endOfToday);

    // Top 5 violators
    List<TopViolatingUserDomain> topViolators = problematicCommentPort.getTopViolatingUsers(5);

    // Convert to response format
    List<DashboardStatResponse.TopViolatorResponse> topViolatorsResponse = topViolators.stream()
        .map(violator -> {
          UserDomain user = violator.getUser();
          Long count = violator.getCommentCount();

          return DashboardStatResponse.TopViolatorResponse.builder()
              .userId(user.getId())
              .username(user.getUsername())
              .commentCount(count)
              .build();
        })
        .collect(Collectors.toList());

    // Build the response
    return DashboardStatResponse.builder()
        .todayCount(todayCount)
        .weeklyCount(weeklyCount)
        .monthlyCount(monthlyCount)
        .totalCount(totalCount)
        .topViolators(topViolatorsResponse)
        .build();
  }

  @Override
  public WeeklyCommentResponse getWeeklyCommentCounts() {
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusWeeks(4).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));

    List<WeeklyCommentResponse.WeeklyData> weeklyStats = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      LocalDate weekStart = startDate.plusWeeks(i);
      LocalDate weekEnd = weekStart.plusDays(6);

      Instant weekStartInstant = weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
      Instant weekEndInstant = weekEnd.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

      Long count = problematicCommentPort.countByDateRange(weekStartInstant, weekEndInstant);

      WeeklyCommentResponse.WeeklyData weekData = WeeklyCommentResponse.WeeklyData.builder()
          .startDate(weekStart.toString())
          .endDate(weekEnd.toString())
          .count(count)
          .build();

      weeklyStats.add(weekData);
    }

    return WeeklyCommentResponse.builder()
        .weeklyStats(weeklyStats)
        .build();
  }

  @Override
  public MonthlyCommentResponse getMonthlyCommentCounts(Instant startDate, Instant endDate) {
    LocalDate today = LocalDate.now();
    LocalDate endLocalDate;
    LocalDate startLocalDate;
    // If both dates are provided, use them
    if (startDate != null && endDate != null) {
      startLocalDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
      endLocalDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate();

      if (endLocalDate.isBefore(startLocalDate)) {
        throw new ClientErrorException("End date cannot be before start date");
      }

      if (endLocalDate.isAfter(today)) {
        throw new ClientErrorException("End date cannot exceed the current month");
      }

      // Validate date range - maximum 6 months
      LocalDate maxAllowedStartDate = endLocalDate.minusMonths(7).plusDays(1);
      if (startLocalDate.isBefore(maxAllowedStartDate)) {
        throw new ClientErrorException("Date range cannot exceed 6 months");
      }
    } else {
      // Default to 6 months if no dates provided
      endLocalDate = today;
      startLocalDate = today.minusMonths(6).withDayOfMonth(1); // 6 months including current month
    }

    List<MonthlyCommentResponse.MonthlyData> monthlyStats = new ArrayList<>();

    // Calculate how many months between start and end dates
    int monthsBetween = (int) (
        endLocalDate.getYear() * 12 + endLocalDate.getMonthValue() -
            (startLocalDate.getYear() * 12 + startLocalDate.getMonthValue())
    );

    for (int i = 0; i < monthsBetween; i++) {
      LocalDate monthStart = startLocalDate.plusMonths(i);
      LocalDate monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());

      // Don't go beyond the end date
      if (monthStart.isAfter(endLocalDate)) {
        break;
      }

      Instant monthStartInstant = monthStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
      Instant monthEndInstant = monthEnd.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

      Long count = problematicCommentPort.countByDateRange(monthStartInstant, monthEndInstant);

      MonthlyCommentResponse.MonthlyData monthData = MonthlyCommentResponse.MonthlyData.builder()
          .month(monthStart.getMonth().toString())
          .year(monthStart.getYear())
          .count(count)
          .build();

      monthlyStats.add(monthData);
    }

    return MonthlyCommentResponse.builder()
        .monthlyStats(monthlyStats)
        .build();
  }

  @Override
  public TopViolatingUsersResponse getTopViolatingUsers(int limit) {
    List<TopViolatingUserDomain> topUsers = problematicCommentPort.getTopViolatingUsers(limit);

    List<TopViolatingUsersResponse.ViolatorData> violators = topUsers.stream()
        .map(violator -> {
          UserDomain user = violator.getUser();
          Long count = violator.getCommentCount();

          return TopViolatingUsersResponse.ViolatorData.builder()
              .userId(user.getId())
              .username(user.getUsername())
              .commentCount(count)
              .build();
        })
        .collect(Collectors.toList());

    return TopViolatingUsersResponse.builder()
        .violators(violators)
        .build();
  }
}
