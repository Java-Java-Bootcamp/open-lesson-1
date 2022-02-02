package ru.otus.javabootcamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ThresholdViolation(String username, ZonedDateTime occurredAt, String message) {

  @Override
  public String toString() {
    return String.format("%s %s",
        occurredAt.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedDateTime(
            FormatStyle.SHORT)),
        message);
  }

}
