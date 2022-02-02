package ru.otus.javabootcamp.api;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryViolationRepository extends JpaRepository<TelemetryViolation, Long> {

  List<TelemetryViolation> findByIsNew(boolean isNew);

}
