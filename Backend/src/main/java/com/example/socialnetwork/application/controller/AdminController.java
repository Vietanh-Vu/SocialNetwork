package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.ConfigCreateRequest;
import com.example.socialnetwork.application.request.ConfigUpdateRequest;
import com.example.socialnetwork.application.response.*;
import com.example.socialnetwork.common.mapper.ProblematicCommentMapper;
import com.example.socialnetwork.domain.model.ProblematicCommentDomain;
import com.example.socialnetwork.domain.port.api.GlobalConfigServicePort;
import com.example.socialnetwork.domain.port.api.ProblematicCommentServicePort;
import com.example.socialnetwork.infrastructure.entity.GlobalConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController extends BaseController {

  private final GlobalConfigServicePort globalConfigService;
  private final ProblematicCommentServicePort problematicCommentService;
  private final ProblematicCommentMapper problematicCommentMapper;

  // Global Config endpoints
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/configs")
  public ResponseEntity<ResultResponse> getAllConfigs(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
      @RequestParam(value = "sort_by", defaultValue = "created") String sortBy,
      @RequestParam(value = "sort_direction", defaultValue = "asc") String sortDirection) {
    Page<GlobalConfig> configs = globalConfigService.getAllConfigs(page, pageSize, sortBy, sortDirection);
    return buildResponse("Get all configs successfully", configs);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/configs/{code}")
  public ResponseEntity<ResultResponse> getConfigByCode(@PathVariable String code) {
    List<GlobalConfig> config = globalConfigService.getConfigByCode(code);
    return buildResponse("Get config successfully", config);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/configs/{code}")
  public ResponseEntity<ResultResponse> updateConfig(
      @PathVariable String code,
      @RequestBody ConfigUpdateRequest request) {
    GlobalConfig config = globalConfigService.updateConfig(code, request.getValue());
    return buildResponse("Update config successfully", config);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/configs")
  public ResponseEntity<ResultResponse> createConfig(@Valid @RequestBody ConfigCreateRequest request) {
    GlobalConfig configDomain = new GlobalConfig();
    configDomain.setName(request.getName());
    configDomain.setCode(request.getCode());
    configDomain.setDesc(request.getValue());

    GlobalConfig config = globalConfigService.createConfig(configDomain);
    return buildResponse("Config created successfully", config);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/configs/{code}")
  public ResponseEntity<ResultResponse> deleteConfig(@PathVariable String code) {
    globalConfigService.deleteConfig(code);
    return buildResponse("Config deleted successfully", Map.of("code", code));
  }

  // Problematic Comment endpoints
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/problematic-comments")
  public ResponseEntity<ResultResponse> getProblematicComments(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
      @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
      @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
      @RequestParam(required = false) Double minProbability,
      @RequestParam(required = false) Double maxProbability,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    Instant startInstant = startDate != null ?
        startDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
    Instant endInstant = endDate != null ?
        endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant() : null;

    Page<ProblematicCommentDomain> comments = problematicCommentService.getFilteredProblematicComments(
        minProbability, maxProbability, startInstant, endInstant, page, pageSize, sortBy, sortDirection);

    // Convert domain objects to flattened response objects
    Page<ProblematicCommentResponse> responseComments = comments.map(problematicCommentMapper::toProblematicCommentResponse);

    return buildResponse("Get problematic comments successfully", responseComments);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/problematic-comments/export")
  public ResponseEntity<InputStreamResource> exportToExcel(
      @RequestParam(required = false) @Min(0) @Max(1) Double minProbability,
      @RequestParam(required = false) @Min(0) @Max(1) Double maxProbability) throws IOException {

    ByteArrayInputStream stream = problematicCommentService.exportToExcel(minProbability, maxProbability);

    LocalDate today = LocalDate.now();
    String filename = "problematic_comments_" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(new InputStreamResource(stream));
  }

  // Dashboard and Statistics endpoints
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/dashboard")
  public ResponseEntity<ResultResponse> getDashboardStats() {
    DashboardStatResponse stats = problematicCommentService.getDashboardStats();
    return buildResponse("Get dashboard stats successfully", stats);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/stats/weekly")
  public ResponseEntity<ResultResponse> getWeeklyStats() {
    WeeklyCommentResponse stats = problematicCommentService.getWeeklyCommentCounts();
    return buildResponse("Get weekly stats successfully", stats);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/stats/monthly")
  public ResponseEntity<ResultResponse> getMonthlyStats(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
  ) {
    MonthlyCommentResponse stats = problematicCommentService.getMonthlyCommentCounts(startDate, endDate);
    return buildResponse("Get monthly stats successfully", stats);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/stats/top-violators")
  public ResponseEntity<ResultResponse> getTopViolators(
      @RequestParam(defaultValue = "10") int limit) {
    TopViolatingUsersResponse topUsers = problematicCommentService.getTopViolatingUsers(limit);
    return buildResponse("Get top violating users successfully", topUsers);
  }
}