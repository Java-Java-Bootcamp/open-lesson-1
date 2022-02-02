package ru.otus.javabootcamp;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


@Slf4j
public class ListNotificationApiQuery {

  final OkHttpClient httpClient = new OkHttpClient();
  final String apiUrl = "http://localhost:8080";

  public List<HeartBeatsNotification> execute(String user) throws ApiCallException {
    Request request = new Request.Builder().url(apiUrl + "/notifications/" + user).build();

    List<HeartBeatsNotification> notifications = List.of();

    try (Response response = httpClient.newCall(request).execute()) {

      if (!response.isSuccessful()) {
        log.error(response.toString());
        throw new ApiCallException(response.toString());
      }

      ResponseBody body = response.body();

      if (body != null) {
        String content = body.string();
        log.debug("Got the response {}", content);
        notifications = NotificationMapper.mapStringToList(content);
      }

    } catch (IOException exception) {
      throw new ApiCallException(exception);
    }

    return notifications;
  }
}
