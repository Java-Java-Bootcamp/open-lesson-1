package ru.otus.javabootcamp;

public class Notification {

  public final String type;
  public final Integer maxHeartBeats;
  public final Integer durationInMinutes;

  public Notification(String type, Integer maxHeartBeats, Integer durationInMinutes) {
    this.type = type;
    this.maxHeartBeats = maxHeartBeats;
    this.durationInMinutes = durationInMinutes;
  }

}
