package ru.otus.javabootcamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class NotificationMapper {

  private final static ObjectMapper mapper = new ObjectMapper();

  public static List<HeartBeatsNotification> mapStringToList(String source) throws JsonProcessingException {
    TypeReference<List<HeartBeatsNotification>> typeRef
        = new TypeReference<>() {
    };
    return mapper.readValue(source, typeRef);
  }

}
