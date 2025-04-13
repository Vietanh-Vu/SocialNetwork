package org.example.kafkaconsumer.share.enums;

public enum AvroMessageOpEnum {
  UPDATE("u"),
  DELETE("d"),
  CREATE("c");

  final String op;

  AvroMessageOpEnum(String op) {
    this.op = op;
  }

  public String getValue() {
    return this.op;
  }

  public static AvroMessageOpEnum getByValue(String value) {
    for (AvroMessageOpEnum opItem : AvroMessageOpEnum.values()) {
      if (opItem.getValue().equals(value)) {
        return opItem;
      }
    }
    return null;
  }
}
