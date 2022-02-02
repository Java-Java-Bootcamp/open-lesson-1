package ru.otus.javabootcamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Slf4j
public class DeleteNotificationApiQuery {

  private final OkHttpClient httpClient = new OkHttpClient();
  private final String apiUrl = "http://localhost:8080";

  private final ObjectMapper mapper = new ObjectMapper();

  public void execute(String user, HeartBeatsNotification notification)
      throws ApiCallException {
    RequestBody body;
    try {
      body = RequestBody.create(mapper.writeValueAsString(notification),
          MediaType.parse("application/json")
      );
    } catch (JsonProcessingException e) {
      throw new ApiCallException(e);
    }

    Request request = new Request.Builder().url(apiUrl + "/notifications/" + user).delete(body
    ).build();

    try (Response response = httpClient.newCall(request).execute()) {

      if (!response.isSuccessful()) {
        log.error(response.toString());
        throw new ApiCallException(response.toString());
      }

    } catch (IOException exception) {
      throw new ApiCallException(exception);
    }

  }
}
