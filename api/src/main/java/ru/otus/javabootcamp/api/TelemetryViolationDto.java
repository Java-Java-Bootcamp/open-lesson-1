package ru.otus.javabootcamp.api;

import java.time.ZonedDateTime;

public record TelemetryViolationDto(String username, ZonedDateTime occurredAt, String message) {

}
