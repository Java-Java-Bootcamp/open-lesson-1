package ru.otus.javabootcamp.botapi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedMessageRepository extends JpaRepository<ReceivedMessage, Long> {

}
