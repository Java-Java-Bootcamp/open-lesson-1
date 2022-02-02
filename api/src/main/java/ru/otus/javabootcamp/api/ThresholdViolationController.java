package ru.otus.javabootcamp.api;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/threshold-violations")
@RequiredArgsConstructor
public class ThresholdViolationController {

  private final TelemetryViolationRepository violationRepository;

  @Transactional
  @GetMapping({"", "/"})
  public ResponseEntity<List<TelemetryViolationDto>> findNew() {
    List<TelemetryViolation> newViolations = violationRepository.findByIsNew(true);

    List<TelemetryViolationDto> newViolationDtos = newViolations.stream()
        .map(TelemetryViolationMapper.INSTANCE::mapToDto).toList();

    newViolations.forEach(v -> v.setNew(false));
    violationRepository.saveAll(newViolations);

    return ResponseEntity.ok(newViolationDtos);
  }

}
