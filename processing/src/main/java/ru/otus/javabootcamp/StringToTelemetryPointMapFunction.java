package ru.otus.javabootcamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringToTelemetryPointMapFunction extends RichFlatMapFunction<String, TelemetryPoint> {

  private static final Logger logger = LoggerFactory.getLogger(StringToTelemetryPointMapFunction.class);
  private final static ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.registerModule(new JavaTimeModule());
  }

  @Override
  public void flatMap(String s, Collector<TelemetryPoint> collector) {
    try {
      if (StringUtils.isNotEmpty(s)) {
        TelemetryPoint point = mapper.readValue(s, TelemetryPoint.class);
        collector.collect(point);
      }
    } catch (JsonProcessingException e) {
      logger.error("Error during parsing telemetry", e);
    }
  }
}
