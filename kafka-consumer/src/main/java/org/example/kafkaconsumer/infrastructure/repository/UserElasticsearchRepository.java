package org.example.kafkaconsumer.infrastructure.repository;

import org.example.kafkaconsumer.infrastructure.entity.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserElasticsearchRepository extends ElasticsearchRepository<UserDocument, Long> {
    Optional<UserDocument> findByUserId(Long userId);
    void deleteByUserIdIs(Long userId);
}
