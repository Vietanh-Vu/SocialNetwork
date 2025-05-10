package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.infrastructure.entity.GlobalConfig;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GlobalConfigServicePort {
  Page<GlobalConfig> getAllConfigs(int page, int pageSize, String sortBy, String sortDirection);
  List<GlobalConfig> getAllConfigs();
  GlobalConfig getConfigByCode(String code);
  GlobalConfig updateConfig(String code, String value);
  GlobalConfig createConfig(GlobalConfig configDomain);
}
