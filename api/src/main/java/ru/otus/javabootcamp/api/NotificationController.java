package ru.otus.javabootcamp.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

  private final NotificationRepository notificationRepository;

  @GetMapping({"/{user}", "/{user}/"})
  @Transactional
  public ResponseEntity<List<NotificationDto>> findAll(@PathVariable String user) {
    var notifications = notificationRepository.findByUsername(user);

    return ResponseEntity.ok(
        notifications.stream().map(NotificationMapper.INSTANCE::mapToDto).collect(Collectors.toList()));
  }

  @PostMapping({"/{user}", "/{user}/"})
  @Transactional
  public ResponseEntity<NotificationDto> create(@PathVariable String user,
      @RequestBody NotificationDto notificationDto) {

    var notification = new Notification(null, NotificationTypes.HEART_BEATS_THRESHOLD_VIOLATION, user,
        notificationDto.maxHeartBeats(), notificationDto.durationInMinutes());

    notification = notificationRepository.save(notification);

    return ResponseEntity.ok(NotificationMapper.INSTANCE.mapToDto(notification));
  }

  @DeleteMapping({"/{user}", "/{user}/"})
  @Transactional
  public ResponseEntity<?> delete(@PathVariable String user,
      @RequestBody NotificationDto notificationDto) {

    Optional<Notification> notification = notificationRepository.findByUsernameAndMaxHeartBeatsAndDurationInMinutes(
        user,
        notificationDto.maxHeartBeats(), notificationDto.durationInMinutes());

    if (notification.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    notificationRepository.delete(notification.get());

    return ResponseEntity.ok().build();
  }
}
