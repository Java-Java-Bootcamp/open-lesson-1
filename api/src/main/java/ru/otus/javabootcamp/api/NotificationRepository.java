package ru.otus.javabootcamp.api;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByUsername(String username);

  Optional<Notification> findByUsernameAndMaxHeartBeatsAndDurationInMinutes(String username, Integer maxHeartBeats,
      Integer durationInMinutes);

}
