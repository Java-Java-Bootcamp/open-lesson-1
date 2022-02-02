package ru.otus.javabootcamp;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class App {

  public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    TelemetryProcessingWorker worker = new TelemetryProcessingWorker(env);
    worker.run();
  }

}
