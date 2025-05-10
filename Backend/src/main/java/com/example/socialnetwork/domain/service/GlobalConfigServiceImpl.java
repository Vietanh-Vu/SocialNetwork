package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.domain.port.api.GlobalConfigServicePort;
import com.example.socialnetwork.domain.port.spi.GlobalConfigDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.GlobalConfig;
import com.example.socialnetwork.infrastructure.repository.GlobalConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalConfigServiceImpl implements GlobalConfigServicePort {
  private final GlobalConfigRepository globalConfigRepository;
  private final GlobalConfigDatabasePort globalConfigDatabasePort;

  @Override
  public Page<GlobalConfig> getAllConfigs(int page, int pageSize, String sortBy, String sortDirection) {
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
    return globalConfigRepository.findAll(pageable);
  }

  @Override
  public List<GlobalConfig> getAllConfigs() {
    return globalConfigRepository.findAll();
  }

  @Override
  public GlobalConfig getConfigByCode(String code) {
    GlobalConfig config = globalConfigDatabasePort.findByCode(code);
    if (config == null) {
      throw new NotFoundException("Config with code " + code + " not found");
    }
    return config;
  }

  @Override
  public GlobalConfig updateConfig(String code, String value) {
    GlobalConfig config = globalConfigDatabasePort.findByCode(code);
    if (config == null) {
      throw new NotFoundException("Config with code " + code + " not found");
    }

    config.setDesc(value);
    config.setCreated(Instant.now());
    return globalConfigRepository.save(config);
  }

  @Override
  public GlobalConfig createConfig(GlobalConfig configDomain) {
    configDomain.setCreated(Instant.now());

    return globalConfigRepository.save(configDomain);
  }
}
