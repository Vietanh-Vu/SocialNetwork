package org.example.kafkaconsumer.share.enums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CDCEvent {
  private Schema schema;
  private Payload payload;

  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Schema {
    private String type;
    private Field[] fields;
    private boolean optional;
    private String name;
    private int version;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Field {
      private String type;
      private boolean optional;
      private String field;
      private String name;
      private int version;
      private Parameters parameters;

      @Getter
      @Setter
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class Parameters {
        private String allowed;
      }
    }
  }

  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Payload {
    private UserData before;
    private UserData after;
    private Source source;
    private String op;
    private long ts_ms;
    private Object transaction;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserData {
      private Long user_id;
      private String username;
      private String email;
      private String password;
      private String first_name;
      private String last_name;
      private String gender;
      private String visibility;
      private Long role_id;
      private String bio;
      private String location;
      private String work;
      private String education;
      private Long created_at;
      private Long updated_at;
      private String avatar;
      private String background_image;
      private Long date_of_birth;
      private Boolean is_email_verified;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {
      private String version;
      private String connector;
      private String name;
      private long ts_ms;
      private String snapshot;
      private String db;
      private String sequence;
      private String table;
      private long server_id;
      private String gtid;
      private String file;
      private long pos;
      private int row;
      private long thread;
      private String query;
    }
  }
}