package ru.otus.javabootcamp.botapi;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "message", schema = "bot")
@Getter
@Setter
@NoArgsConstructor
public class ReceivedMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String message;
  private ZonedDateTime received;

  public ReceivedMessage(String message) {
    this.message = message;
    this.received = ZonedDateTime.now();
  }
}
