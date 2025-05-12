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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
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
    // Tính toán 12 tháng gần nhất
    LocalDate today = LocalDate.now();
    List<YearMonth> months = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      months.add(YearMonth.from(today.minusMonths(i)));
    }

    // Workbook chung cho tất cả các sheet
    XSSFWorkbook sharedWorkbook = new XSSFWorkbook();

    // Tạo thread pool
    ExecutorService executor = Executors.newFixedThreadPool(
        Math.min(months.size(), Runtime.getRuntime().availableProcessors() * 2)
    );

    try {
      List<Future<Void>> futures = new ArrayList<>();

      for (YearMonth yearMonth : months) {
        Future<Void> future = executor.submit(() -> {
          LocalDate monthStart = yearMonth.atDay(1);
          LocalDate monthEnd = yearMonth.atEndOfMonth();
          Instant startInstant = monthStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
          Instant endInstant = monthEnd.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

          // Lấy dữ liệu từng tháng
          List<ProblematicCommentDomain> monthData = new ArrayList<>();
          int pageSize = 1000;
          int pageNumber = 0;
          Page<ProblematicCommentDomain> commentsPage;

          do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());

            commentsPage = (minProbability != null && maxProbability != null)
                ? problematicCommentPort.getProblematicCommentsByProbabilityAndDateRange(
                minProbability, maxProbability, startInstant, endInstant, pageable)
                : problematicCommentPort.getProblematicCommentsByDateRange(
                startInstant, endInstant, pageable);

            monthData.addAll(commentsPage.getContent());
            pageNumber++;
          } while (commentsPage.hasNext());

          if (monthData.isEmpty()) return null;

          // Ghi vào workbook dùng synchronized để thread-safe
          synchronized (sharedWorkbook) {
            String sheetName = yearMonth.getYear() + "-" + String.format("%02d", yearMonth.getMonthValue());
            Sheet sheet = sharedWorkbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("id");
            headerRow.createCell(1).setCellValue("user_id");
            headerRow.createCell(2).setCellValue("content");
            headerRow.createCell(3).setCellValue("probability");
            headerRow.createCell(4).setCellValue("created_at");

            int rowIdx = 1;
            for (ProblematicCommentDomain comment : monthData) {
              Row row = sheet.createRow(rowIdx++);
              row.createCell(0).setCellValue(comment.getId());
              row.createCell(1).setCellValue(comment.getUser().getId());
              row.createCell(2).setCellValue(comment.getContent());
              row.createCell(3).setCellValue(comment.getSpamProbability());
              row.createCell(4).setCellValue(comment.getCreatedAt().toString());
            }
          }

          return null;
        });

        futures.add(future);
      }

      // Đợi tất cả các task hoàn thành
      for (Future<Void> future : futures) {
        future.get();
      }

      // Ghi workbook vào ByteArrayOutputStream để trả về
      try (ByteArrayOutputStream finalOut = new ByteArrayOutputStream()) {
        sharedWorkbook.write(finalOut);
        return new ByteArrayInputStream(finalOut.toByteArray());
      }

    } catch (Exception e) {
      throw new IOException("Error during export: " + e.getMessage(), e);
    } finally {
      executor.shutdown();
      try {
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
      }

      sharedWorkbook.close(); // Đóng workbook
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
    List<TopViolatingUserDomain> topViolators = problematicCommentPort.getTopViolatingUsers(10);

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
    LocalDate startDate = today.minusWeeks(7).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));

    List<WeeklyCommentResponse.WeeklyData> weeklyStats = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
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
      startLocalDate = today.minusMonths(12).withDayOfMonth(1); // 6 months including current month
    }

    List<MonthlyCommentResponse.MonthlyData> monthlyStats = new ArrayList<>();

    // Calculate how many months between start and end dates
    int monthsBetween =
        endLocalDate.getYear() * 12 + endLocalDate.getMonthValue() -
            (startLocalDate.getYear() * 12 + startLocalDate.getMonthValue());

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
