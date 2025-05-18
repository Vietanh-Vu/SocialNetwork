package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.GlobalConfigConstants;
import com.example.socialnetwork.domain.port.spi.GlobalConfigDatabasePort;
import com.example.socialnetwork.infrastructure.entity.GlobalConfig;
import com.example.socialnetwork.infrastructure.repository.GlobalConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Component
public class GlobalConfigDatabaseAdapter implements GlobalConfigDatabasePort {
  private final GlobalConfigRepository globalConfigRepository;

  @Override
  public Double getHateSpeechThreshold() {
    GlobalConfig config = this.findByCode(GlobalConfigConstants.HATE_SPEECH_THRESHOLD);
    if (Objects.isNull(config)) {
      return GlobalConfigConstants.HATE_SPEECH_THRESHOLD_DEFAULT;
    } else {
      try {
        return Double.parseDouble(config.getDesc());
      } catch (NumberFormatException e) {
        return GlobalConfigConstants.HATE_SPEECH_THRESHOLD_DEFAULT;
      }
    }
  }

  @Override
  public Double getThresholdToImportToProblematicComment() {
    GlobalConfig config =
        this.findByCode(GlobalConfigConstants.THRESHOLD_TO_IMPORT_TO_PROBLEMATIC_COMMENT);
    if (Objects.isNull(config)) {
      return GlobalConfigConstants.THRESHOLD_TO_IMPORT_TO_PROBLEMATIC_COMMENT_DEFAULT;
    } else {
      try {
        return Double.parseDouble(config.getDesc());
      } catch (NumberFormatException e) {
        return GlobalConfigConstants.THRESHOLD_TO_IMPORT_TO_PROBLEMATIC_COMMENT_DEFAULT;
      }
    }
  }

  @Override
  public Integer getStartDetectComment() {
    GlobalConfig config = this.findByCode(GlobalConfigConstants.START_DETECT_COMMENT);
    if (Objects.isNull(config)) {
      return GlobalConfigConstants.START_DETECT_COMMENT_DEFAULT;
    } else {
      try {
        return Integer.parseInt(config.getDesc());
      } catch (NumberFormatException e) {
        return GlobalConfigConstants.START_DETECT_COMMENT_DEFAULT;
      }
    }
  }

  @Override
  public GlobalConfig findByCode(String code) {
    return globalConfigRepository.findByCode(code).orElse(null);
  }

  @Override
  public List<GlobalConfig> findByCodeContaining(String code) {
    return globalConfigRepository.findByCodeContaining(code);
  }
}
