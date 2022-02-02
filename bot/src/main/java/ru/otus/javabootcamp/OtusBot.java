package ru.otus.javabootcamp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class OtusBot extends TelegramLongPollingBot {

  final OkHttpClient httpClient = new OkHttpClient();
  final String apiUrl = "http://localhost:8080";
  final ObjectMapper mapper = new ObjectMapper();


  private final List<Pair<Function<String, Boolean>, MessageHandler>> handlers = List.of(
      Pair.of(messageText -> messageText.startsWith(BotCommands.listCommand), this::handleList),
      Pair.of(messageText -> messageText.startsWith(BotCommands.addCommand), this::handleAdd),
      Pair.of(messageText -> messageText.startsWith(BotCommands.reportCommand), this::handleReport),
      Pair.of(messageText -> messageText.startsWith(BotCommands.deleteCommand), this::handleDelete)
  );


  @Override
  public String getBotUsername() {
    return "JavaBootcampOtus_bot";
  }

  @Override
  public String getBotToken() {
    return "5247199062:AAFXpNDyUYevdDGeffWtoR6mGGUbbD9PMSk";
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {

      Message message = update.getMessage();
      String messageText = message.getText();
      Long chatId = message.getChatId();
      String user = message.getFrom().getUserName();

      try {
        log.debug("Got the message {} from the user {} in the chat {}", messageText, user, chatId);

        var handler = handlers.stream()
            .filter(p -> p.getLeft().apply(messageText)).findFirst();
        if (handler.isPresent()) {
          handler.get().getRight().handle(message);
        } else {
          handleUnknownCommand(message);
        }
      } catch (TelegramApiException e) {
        log.error(String.format("An error occurred during sending message %s", messageText), e);
      }
    }
  }

  private void handleUnknownCommand(Message message) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString())
        .text(String.format("Неизвестная команда: %s", message.getText()))
        .build();

    execute(sendMessage);
  }

  private void handleList(Message message) throws TelegramApiException {

    Request request = new Request.Builder().url(apiUrl + "/notifications").build();

    List<HeartBeatsNotification> notifications = List.of();

    try (Response response = httpClient.newCall(request).execute()) {

      if (!response.isSuccessful()) {
        log.error(response.toString());
        handleError(message, response.toString());
        return;
      }

      ResponseBody body = response.body();

      if (body != null) {
        TypeReference<List<HeartBeatsNotification>> typeRef
            = new TypeReference<>() {
        };

        String content = body.string();

        log.debug("Got the response {}", content);

        notifications = mapper.readValue(content, typeRef);
      }

    } catch (IOException exception) {
      handleError(message, exception.getMessage());

      String errorMessage = String.format(
          "Got error during calling api call [%s]", apiUrl);

      log.error(errorMessage);
    }

    String notificationListMessage =
        notifications.stream().map(Record::toString).collect(Collectors.joining("\n"));

    if (StringUtils.isEmpty(notificationListMessage)) {
      notificationListMessage = "Уведомлений нет";
    }

    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString())
        .text(notificationListMessage)
        .build();
    execute(sendMessage);
  }


  private void handleAdd(Message message) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString()).text("Уведомление добавлено")
        .build();

    execute(sendMessage);
  }

  private void handleDelete(Message message) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString()).text("Уведомление удалено")
        .build();

    execute(sendMessage);
  }

  private void handleReport(Message message) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString()).text("Отчет")
        .build();

    execute(sendMessage);
  }

  private void handleError(Message message, String error) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString())
        .text(String.format("Возникла ошибка: %s", error))
        .build();

    execute(sendMessage);
  }
}
