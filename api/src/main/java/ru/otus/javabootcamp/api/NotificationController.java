package ru.otus.javabootcamp.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

  @GetMapping({"", "/"})
  public ResponseEntity<List<NotificationDto>> findAll() {
    return ResponseEntity.ok(List.of(
        new NotificationDto(1L, "Превышение пульса", 120, 5),
        new NotificationDto(2L, "Превышение пульса", 110, 10)
    ));
  }

}
