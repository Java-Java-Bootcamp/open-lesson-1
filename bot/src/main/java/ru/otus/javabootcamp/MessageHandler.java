package ru.otus.javabootcamp;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@FunctionalInterface
public interface MessageHandler {

  void handle(Message message) throws TelegramApiException;
}
