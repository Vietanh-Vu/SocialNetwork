package com.example.socialnetwork.common.constant;

public class GlobalConfigConstants {
  public static final String HATE_SPEECH_THRESHOLD = "hate_speech_threshold";
  public static final Double HATE_SPEECH_THRESHOLD_DEFAULT = 0.5;

  public static final String THRESHOLD_TO_IMPORT_TO_PROBLEMATIC_COMMENT =
      "threshold_to_import_to_problematic_comment";
  public static final Double THRESHOLD_TO_IMPORT_TO_PROBLEMATIC_COMMENT_DEFAULT = 0.5;

  public static final String START_DETECT_COMMENT = "start_detect_comment";
  public static final Integer START_DETECT_COMMENT_DEFAULT = 0;

  public static final String MAX_SPAM_COUNT = "max_spam_count";
  public static final Integer MAX_SPAM_COUNT_DEFAULT = 5;

  public static final String BAN_DURATION_HOURS = "ban_duration_hours";
  public static final Integer BAN_DURATION_HOURS_DEFAULT = 24;
}
