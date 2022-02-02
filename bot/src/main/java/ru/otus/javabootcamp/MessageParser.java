package ru.otus.javabootcamp;

import java.util.Optional;

public class MessageParser {

  public static Optional<HeartBeatsNotification> parse(String text) {
    String[] parts = text.split(" ");

    if (parts.length < 3) {
      return Optional.empty();
    }

    int maxHeartBeats = Integer.parseInt(parts[1]);
    int durationInMinutes = Integer.parseInt(parts[2]);

    return Optional.of(new HeartBeatsNotification(maxHeartBeats, durationInMinutes));
  }


}
