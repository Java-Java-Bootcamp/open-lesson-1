package ru.otus.javabootcamp.api;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {
  NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

  NotificationDto mapToDto(Notification source);

}
