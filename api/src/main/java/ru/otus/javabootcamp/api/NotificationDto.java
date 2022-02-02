package ru.otus.javabootcamp.api;

public record NotificationDto(Long id, String name, Integer maxHeartBeats, Integer durationInMinutes) {

}
