package ru.otus.javabootcamp;

public class ThresholdViolation {

  public String username;
  public String occurredAt;
  public String message;

  private ThresholdViolation() {
  }

  public ThresholdViolation(String username, String occurredAt, String message) {
    this.username = username;
    this.occurredAt = occurredAt;
    this.message = message;
  }
}
