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
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblematicCommentServiceImpl implements ProblematicCommentServicePort {
  private final ProblematicCommentDatabasePort problematicCommentPort;
  private final UserDatabasePort userDatabasePort;

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
    Page<ProblematicCommentDomain> result;
    if (minProbability != null && maxProbability != null && startDate != null && endDate != null) {
      result = problematicCommentPort.getProblematicCommentsByProbabilityAndDateRange(
          minProbability, maxProbability, startDate, endDate, pageable);
    } else if (minProbability != null && maxProbability != null) {
      result = problematicCommentPort.getProblematicCommentsByProbability(minProbability, maxProbability, pageable);
    } else if (startDate != null && endDate != null) {
      result = problematicCommentPort.getProblematicCommentsByDateRange(startDate, endDate, pageable);
    } else {
      result = problematicCommentPort.getAllProblematicComments(pageable);
    }

    this.enrichUserDomain(result);
    return result;
  }

  @Override
  public ByteArrayInputStream exportToExcel(Double minProbability, Double maxProbability) throws IOException {
    log.info(">>> [ProblematicCommentService] exportToExcel: Starting Excel export process with minProbability={}, maxProbability={}", minProbability, maxProbability);
    // Tính toán 12 tháng gần nhất
    LocalDate today = LocalDate.now();
    List<YearMonth> months = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      months.add(YearMonth.from(today.minusMonths(i)));
    }

    // Workbook chung cho tất cả các sheet, sử dụng SXSSFWorkbook để tối ưu bộ nhớ
    SXSSFWorkbook sharedWorkbook = new SXSSFWorkbook(100);
    sharedWorkbook.setCompressTempFiles(true);

    // Tạo thread pool với số lượng thread bằng số tháng hoặc số CPU * 2 (lấy giá trị nhỏ hơn)
    ExecutorService executor = Executors.newFixedThreadPool(
        Math.min(months.size(), Runtime.getRuntime().availableProcessors() * 2)
    );

    try {
      // Tạo các sheet và header trước
      Map<String, Sheet> sheets = new HashMap<>();
      for (YearMonth yearMonth : months) {
        String sheetName = yearMonth.getYear() + "-" + String.format("%02d", yearMonth.getMonthValue());
        Sheet sheet = sharedWorkbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("id");
        headerRow.createCell(1).setCellValue("user_id");
        headerRow.createCell(2).setCellValue("username");
        headerRow.createCell(3).setCellValue("content");
        headerRow.createCell(4).setCellValue("probability");
        headerRow.createCell(5).setCellValue("created_at");

        sheets.put(sheetName, sheet);
      }

      // Tạo danh sách các Future để theo dõi tiến trình
      List<Future<?>> futures = new ArrayList<>();

      // Xử lý song song cho từng tháng bằng JDBC stream
      for (YearMonth yearMonth : months) {
        Future<?> future = executor.submit(() -> {
          LocalDate monthStart = yearMonth.atDay(1);
          LocalDate monthEnd = yearMonth.atEndOfMonth();
          log.info(">>> [ProblematicCommentService] exportToExcel: Processing month {}-{}", yearMonth.getYear(), yearMonth.getMonthValue());
          Instant startInstant = monthStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
          Instant endInstant = monthEnd.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

          String sheetName = yearMonth.getYear() + "-" + String.format("%02d", yearMonth.getMonthValue());
          Sheet sheet = sheets.get(sheetName);

          // Theo dõi số hàng trong sheet
          final int[] rowIdx = {1};

          // Sử dụng JDBC stream trực tiếp
          problematicCommentPort.streamProblematicCommentsByProbabilityAndDateRange(
              minProbability, maxProbability, startInstant, endInstant,
              rs -> {
                try {
                  while (rs.next()) {
                    Row row = sheet.createRow(rowIdx[0]++);
                    row.createCell(0).setCellValue(rs.getLong("id"));
                    row.createCell(1).setCellValue(rs.getLong("user_id"));
                    row.createCell(2).setCellValue(rs.getString("content"));
                    row.createCell(3).setCellValue(rs.getDouble("spam_probability"));
                    row.createCell(4).setCellValue(rs.getTimestamp("created_at").toString());
                  }
                } catch (SQLException e) {
                  throw new RuntimeException("Error processing result set", e);
                }
                return null;
              }
          );
          log.info(">>> [ProblematicCommentService] exportToExcel: Finished processing month {}-{}", yearMonth.getYear(), yearMonth.getMonthValue());
          return null;
        });

        futures.add(future);
      }

      // Đợi tất cả các task hoàn thành
      for (Future<?> future : futures) {
        try {
          future.get();
        } catch (InterruptedException | ExecutionException e) {
          throw new IOException("Error during parallel processing: " + e.getMessage(), e);
        }
      }

      // Ghi workbook vào ByteArrayOutputStream để trả về
      log.info(">>> [ProblematicCommentService] exportToExcel: Writing workbook to output stream");
      try (ByteArrayOutputStream finalOut = new ByteArrayOutputStream()) {
        sharedWorkbook.write(finalOut);
        log.info(">>> [ProblematicCommentService] exportToExcel: Finished writing workbook to output stream");
        return new ByteArrayInputStream(finalOut.toByteArray());
      }
    } catch (Exception e) {
      throw new IOException("Error during export: " + e.getMessage(), e);
    } finally {
      // Shutdown thread pool
      executor.shutdown();
      try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
      }

      // Dọn dẹp tài nguyên tạm thời của SXSSFWorkbook
      sharedWorkbook.dispose();
      sharedWorkbook.close();
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

  private void enrichUserDomain(Page<ProblematicCommentDomain> comments) {
    Set<Long> userIds = comments.getContent().stream()
        .map(comment -> comment.getUser().getId())
        .collect(Collectors.toSet());

    List<UserDomain> users = userDatabasePort.findAllByIds(new ArrayList<>(userIds));

    Map<Long, UserDomain> userMap = users.stream()
        .collect(Collectors.toMap(UserDomain::getId, Function.identity()));

    // 4. Enrich each comment with complete user information
    comments.getContent().forEach(comment -> {
      Long userId = comment.getUser().getId();
      if (userMap.containsKey(userId)) {
        UserDomain user = userMap.get(userId);
        UserDomain enrichedUser = UserDomain.builder()
            .id(user.getId())
            .username(user.getUsername())
            .avatar(user.getAvatar())
            .build();
        comment.setUser(enrichedUser);
      }
    });
  }
}
