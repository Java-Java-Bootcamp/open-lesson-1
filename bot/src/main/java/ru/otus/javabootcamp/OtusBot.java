package ru.otus.javabootcamp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class OtusBot extends TelegramLongPollingBot {

  private final ListNotificationApiQuery listQuery = new ListNotificationApiQuery();
  private final AddNotificationApiQuery addNotificationApiQuery = new AddNotificationApiQuery();
  private final DeleteNotificationApiQuery deleteNotificationApiQuery = new DeleteNotificationApiQuery();
  private final ThresholdViolationApiQuery violationApiQuery = new ThresholdViolationApiQuery();

  private final Map<String, Long> users = new HashMap<>();

  private final List<Pair<Function<String, Boolean>, MessageHandler>> handlers = List.of(
      Pair.of(messageText -> messageText.startsWith(BotCommands.listCommand), this::handleList),
      Pair.of(messageText -> messageText.startsWith(BotCommands.addCommand), this::handleAdd),
      Pair.of(messageText -> messageText.startsWith(BotCommands.reportCommand), this::handleReport),
      Pair.of(messageText -> messageText.startsWith(BotCommands.deleteCommand), this::handleDelete)
  );

  public OtusBot() {
    super();
    startAlertTimers();
  }


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

      users.put(user, chatId);

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

  private void startAlertTimers() {
    ScheduledExecutor executor = new ScheduledExecutor(() -> {
      try {
        List<ThresholdViolation> violations = violationApiQuery.execute();

        violations.forEach(violation -> {
          if (users.containsKey(violation.username())) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(users.get(violation.username())));
            sendMessage.setText(violation.toString());
            try {
              execute(sendMessage);
            } catch (TelegramApiException e) {
              log.error(String.format("Error during sending violations to %s", violation.username()), e);
            }
          } else {
            log.warn("Could not find chat for user {}", violation.username());
          }
        });


      } catch (ApiCallException e) {
        log.error("Error during querying violations", e);
      }
    }, 10);

    executor.start();
  }

  private void handleUnknownCommand(Message message) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString())
        .text(String.format("?????????????????????? ??????????????: %s", message.getText()))
        .build();

    execute(sendMessage);
  }

  private void handleList(Message message) throws TelegramApiException {

    List<HeartBeatsNotification> notifications = List.of();

    try {
      notifications = listQuery.execute(message.getFrom().getUserName());

    } catch (ApiCallException exception) {
      log.error("Error during querying", exception);
      handleError(message, exception.getMessage());
    }

    String notificationListMessage =
        notifications.stream().map(Record::toString).collect(Collectors.joining("\n"));

    if (StringUtils.isEmpty(notificationListMessage)) {
      notificationListMessage = "?????????????????????? ??????";
    }

    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString())
        .text(notificationListMessage)
        .build();
    execute(sendMessage);
  }


  private void handleAdd(Message message) throws TelegramApiException {
    Optional<HeartBeatsNotification> notification = MessageParser.parse(message.getText());

    if (notification.isEmpty()) {
      handleError(message, "???????????????????????? ??????????????????");
      return;
    }
    String user = message.getFrom().getUserName();

    try {
      addNotificationApiQuery.execute(user, notification.get());

    } catch (ApiCallException exception) {
      log.error("Error during querying", exception);
      handleError(message, exception.getMessage());
    }

    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString()).text("?????????????????????? ??????????????????")
        .build();

    execute(sendMessage);
  }

  private void handleDelete(Message message) throws TelegramApiException {
    Optional<HeartBeatsNotification> notification = MessageParser.parse(message.getText());

    if (notification.isEmpty()) {
      handleError(message, "???????????????????????? ??????????????????");
      return;
    }
    String user = message.getFrom().getUserName();

    try {
      deleteNotificationApiQuery.execute(user, notification.get());

    } catch (ApiCallException exception) {
      log.error("Error during querying", exception);
      handleError(message, exception.getMessage());
    }

    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString()).text("?????????????????????? ??????????????")
        .build();

    execute(sendMessage);
  }

  private void handleReport(Message message) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString()).text("??????????")
        .build();

    execute(sendMessage);
  }

  private void handleError(Message message, String error) throws TelegramApiException {
    SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId().toString())
        .text(String.format("???????????????? ????????????: %s", error))
        .build();

    execute(sendMessage);
  }
}
