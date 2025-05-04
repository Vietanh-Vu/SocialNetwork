package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.infrastructure.entity.GlobalConfig;

public interface GlobalConfigDatabasePort {
  Double getHateSpeechThreshold();
  Double getThresholdToImportToProblematicComment();
  Integer getStartDetectComment();
  GlobalConfig findByCode(String code);
}
