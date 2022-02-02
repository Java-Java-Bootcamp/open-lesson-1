package ru.otus.javabootcamp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {

  public List<Notification> findUserNotifications(String username) throws SQLException {
    List<Notification> notifications = new ArrayList<>();
    PostgreClient client = new PostgreClient();
    client.connect();

    ResultSet result = client.executeSelect(String.format("select type, max_heart_beats, duration_in_minutes\n"
        + "from api.notifications\n"
        + "where username = '%s'", username));

    while (result.next()) {
      notifications.add(new Notification(result.getString(1), result.getInt(2), result.getInt(3)));
    }

    client.close();

    return notifications;
  }

}
