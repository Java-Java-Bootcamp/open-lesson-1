package ru.otus.javabootcamp.api;

public record NotificationDto(Long id, String type, String username, Integer maxHeartBeats, Integer durationInMinutes) {

}
