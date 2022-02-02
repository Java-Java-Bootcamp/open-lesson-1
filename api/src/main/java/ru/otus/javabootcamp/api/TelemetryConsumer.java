package ru.otus.javabootcamp.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "telemetry-consumer", topics = {"telemetry-violation"})
@Slf4j
@RequiredArgsConstructor
public class TelemetryConsumer {

  private final ObjectMapper mapper;
  private final TelemetryViolationRepository repository;

  @KafkaHandler
  public void consume(String data) {
    try {
      TelemetryViolationDto dto = mapper.readValue(data, TelemetryViolationDto.class);
      TelemetryViolation violation = TelemetryViolationMapper.INSTANCE.mapFromDto(dto);
      repository.save(violation);
    } catch (JsonProcessingException e) {
      log.warn("Got the message which is not in the violation format: {}", data);
    }
  }
}
