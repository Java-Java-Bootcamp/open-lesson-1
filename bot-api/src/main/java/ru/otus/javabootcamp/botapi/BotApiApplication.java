package ru.otus.javabootcamp.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Slf4j
public class BotApiApplication implements ApplicationRunner {

  @Autowired
  MyBot bot;

  public static void main(String[] args) {

    SpringApplication.run(BotApiApplication.class, args);

  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    try {
      TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
      api.registerBot(bot);
    } catch (TelegramApiException e) {
      log.error("An error occurred during register bot", e);
    }
  }
}
