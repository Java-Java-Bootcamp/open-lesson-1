package ru.otus.javabootcamp.api;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "telemetry_violation", schema = "api")
@Getter
@Setter
public class TelemetryViolation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  @Column(name = "occurred_at")
  private ZonedDateTime occurredAt;

  private String message;

  @Column(name = "is_new")
  private boolean isNew = Boolean.TRUE;
}
