package ru.otus.javabootcamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

public class ThresholdViolationMapper {

  private final static ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.registerModule(new JavaTimeModule());
  }

  public static List<ThresholdViolation> mapStringToList(String source) throws JsonProcessingException {
    TypeReference<List<ThresholdViolation>> typeRef
        = new TypeReference<>() {
    };
    return mapper.readValue(source, typeRef);
  }

}
