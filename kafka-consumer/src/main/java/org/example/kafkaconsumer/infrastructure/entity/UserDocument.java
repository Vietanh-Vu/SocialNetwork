package org.example.kafkaconsumer.infrastructure.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.kafkaconsumer.share.enums.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Document(indexName = "users")
@Setting(settingPath = "static/es-settings.json")
@Getter
@Setter
@Builder
public class UserDocument {
  @Id
  private String id;

  @Field(type = FieldType.Long)
  private Long userId;

  @Field(type = FieldType.Text, analyzer = "autocomplete", searchAnalyzer = "search_analyzer")
  private String username;

  @Field(type = FieldType.Keyword)
  private String email;

  @Field(type = FieldType.Keyword)
  private String password;

  @Field(type = FieldType.Keyword)
  private String firstName;

  @Field(type = FieldType.Keyword)
  private String lastName;

  @Field(type = FieldType.Keyword)
  private Gender gender;

  @Field(type = FieldType.Text)
  private String visibility;

  @Field(type = FieldType.Keyword)
  private String role;

  @Field(type = FieldType.Text)
  private String bio;

  @Field(type = FieldType.Text)
  private String location;

  @Field(type = FieldType.Text)
  private String work;

  @Field(type = FieldType.Text)
  private String education;

  @Field(type = FieldType.Date)
  private Instant createdAt;

  @Field(type = FieldType.Date)
  private Instant updatedAt;

  @Field(type = FieldType.Keyword)
  private String avatar;

  @Field(type = FieldType.Keyword)
  private String backgroundImage;

  @Field(type = FieldType.Date)
  private LocalDate dateOfBirth;

  @Field(type = FieldType.Boolean)
  private Boolean isEmailVerified;

  @Field(type = FieldType.Long)
  private List<Long> friendIds;
}
