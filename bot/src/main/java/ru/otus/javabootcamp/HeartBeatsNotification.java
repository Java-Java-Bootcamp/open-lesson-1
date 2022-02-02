package ru.otus.javabootcamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HeartBeatsNotification(Integer maxHeartBeats, Integer durationInMinutes) {

  @Override
  public String toString() {
    return String.format("Первышение максимального пульса %d в течение %d минут", maxHeartBeats, durationInMinutes);
  }

}
