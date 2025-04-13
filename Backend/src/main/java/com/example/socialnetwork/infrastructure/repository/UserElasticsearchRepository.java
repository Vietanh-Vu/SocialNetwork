package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.elasticsearch.UserDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserElasticsearchRepository extends ElasticsearchRepository<UserDocument, Long> {
  @Query("{\"match\": {\"username\": \"?0\"}}")
  List<UserDocument> findByUsernameContaining(String username);

  @Query("""
        {
          "bool": {
            "must": [
              { "term": { "friendIds": ?0 } },
              { "match": { "username": "?1" } }
            ]
          }
        }
    """)
  List<UserDocument> findFriendsByKeyword(Long userId, String keyword);
}
