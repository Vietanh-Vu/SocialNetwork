package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.infrastructure.entity.GlobalConfig;

import java.util.List;

public interface GlobalConfigDatabasePort {
  Double getHateSpeechThreshold();
  Double getThresholdToImportToProblematicComment();
  Integer getStartDetectComment();
  Integer getMaxSpamCount();
  Integer getBanDurationHours();
  GlobalConfig findByCode(String code);
  List<GlobalConfig> findByCodeContaining(String code);
}
