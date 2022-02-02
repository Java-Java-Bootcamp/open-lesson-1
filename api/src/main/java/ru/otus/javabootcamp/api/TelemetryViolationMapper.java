package ru.otus.javabootcamp.api;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TelemetryViolationMapper {
  TelemetryViolationMapper INSTANCE = Mappers.getMapper(TelemetryViolationMapper.class);

  TelemetryViolation mapFromDto(TelemetryViolationDto dto);
  TelemetryViolationDto mapToDto(TelemetryViolation source);

}
