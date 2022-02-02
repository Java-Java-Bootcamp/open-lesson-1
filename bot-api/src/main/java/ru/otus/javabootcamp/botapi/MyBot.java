package ru.otus.javabootcamp.botapi;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Service
@RequiredArgsConstructor
public class MyBot extends TelegramLongPollingBot {

  private final ReceivedMessageRepository messageRepository;

  @Override
  public String getBotUsername() {
    return "JavaBootcampOtus_bot";
  }

  @Override
  public String getBotToken() {
    return "5247199062:AAFXpNDyUYevdDGeffWtoR6mGGUbbD9PMSk";
  }

  @Override
  @Transactional
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      Message message = update.getMessage();
      String messageText = message.getText();
      Long chatId = message.getChatId();

      SendMessage sendMessage = SendMessage.builder().chatId(chatId.toString()).text(messageText).build();

      try {
        log.debug("Got the message {}", messageText);
        messageRepository.save(new ReceivedMessage(messageText));
        System.out.println("Got the message");
        execute(sendMessage);

        log.debug("Sent the message {}", messageText);
      } catch (TelegramApiException e) {
        log.error(String.format("An error occurred during sending message %s", messageText), e);
      }
    }
  }
}
