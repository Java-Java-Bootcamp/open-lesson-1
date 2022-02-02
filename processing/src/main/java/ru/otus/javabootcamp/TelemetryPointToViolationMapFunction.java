package ru.otus.javabootcamp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelemetryPointToViolationMapFunction extends RichFlatMapFunction<TelemetryPoint, ThresholdViolation> {

  private static final Logger logger = LoggerFactory.getLogger(TelemetryPointToViolationMapFunction.class);
  private final static ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.registerModule(new JavaTimeModule());
  }

  private transient MapState<String, TelemetryPoint> violationsState;
  private NotificationRepository repository;

  @Override
  public void flatMap(TelemetryPoint point, Collector<ThresholdViolation> collector) throws Exception {
    List<Notification> notifications = repository.findUserNotifications(point.username);

    logger.info("The user {} has {} notifications", point.username, notifications.size());

    List<Notification> violatedNotifications = notifications.stream().filter(n -> n.maxHeartBeats < point.heartBeats)
        .collect(Collectors.toList());

    if (violatedNotifications.size() == 0) {
      logger.info("The user {} has no notifications. Clear its state", point.username);
      violationsState.remove(point.username);
      return;
    }

    if (violationsState.contains(point.username)) {
      ZonedDateTime violationStarted = ZonedDateTime.parse(violationsState.get(point.username).occurredAt);
      ZonedDateTime pointTime = ZonedDateTime.parse(point.occurredAt);

      long duration = Duration.between(violationStarted, pointTime).toMinutes();

      logger.info("Duration is {}", duration);

      List<Notification> quiteLongNotifications = violatedNotifications.stream()
          .filter(n -> n.durationInMinutes < duration).collect(Collectors.toList());

      quiteLongNotifications.forEach(n -> {
        collector.collect(
            new ThresholdViolation(point.username, violationStarted.format(DateTimeFormatter.ISO_DATE_TIME),
                String.format("Первышение максимальног пульса %d в течение %d минут",
                    n.maxHeartBeats, duration)));
      });

      if (!quiteLongNotifications.isEmpty()) {
        logger.info("All violations of the user {} were collected. Clear its state", point.username);
        violationsState.remove(point.username);
      } else {
        logger.info("There are not quite long violation for the user {}", point.username);
      }
    } else {
      logger.info("Found the first violation. Setup the user {} state", point.username);
      violationsState.put(point.username, point);
    }
  }

  @Override
  public void open(Configuration config) {
    MapStateDescriptor<String, TelemetryPoint> descriptorViolationState =
        new MapStateDescriptor<>(
            "threshold-violation-state",
            BasicTypeInfo.STRING_TYPE_INFO,
            TypeInformation.of(new TypeHint<TelemetryPoint>() {
            }));
    violationsState = getRuntimeContext().getMapState(descriptorViolationState);

    if (repository == null) {
      repository = new NotificationRepository();
    }
  }
}
