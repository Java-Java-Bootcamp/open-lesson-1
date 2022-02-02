package ru.otus.javabootcamp;

import java.io.Serializable;

public class TelemetryPoint implements Serializable {

  private static final long serialVersionUID = 1L;

  public String username;
  public String occurredAt;
  public Integer heartBeats;

  private TelemetryPoint() {
  }

  TelemetryPoint(String username,
      String occurredAt,
      Integer heartBeats) {
    this.username = username;
    this.occurredAt = occurredAt;
    this.heartBeats = heartBeats;
  }

}
