package ru.otus.javabootcamp;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public final class BotApp {

  public static void main(String[] args) {
    try {
      TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
      api.registerBot(new OtusBot());
      log.info("The OTUS's telegram bot started");
    } catch (TelegramApiException e) {
      log.error("An error occurred during register bot", e);
    }
  }
}
