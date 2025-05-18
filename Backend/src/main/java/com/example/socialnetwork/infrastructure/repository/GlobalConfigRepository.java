package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.GlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GlobalConfigRepository extends JpaRepository<GlobalConfig, Integer> {
  Optional<GlobalConfig> findByCode(String code);
  List<GlobalConfig> findByCodeContaining(String code);

  void deleteByCode(String code);
}