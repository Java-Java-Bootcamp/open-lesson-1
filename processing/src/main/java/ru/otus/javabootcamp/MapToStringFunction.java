package ru.otus.javabootcamp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.functions.MapFunction;

public class MapToStringFunction implements MapFunction<ThresholdViolation, String> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String map(ThresholdViolation thresholdViolation) throws Exception {
    return mapper.writeValueAsString(thresholdViolation);
  }
}
