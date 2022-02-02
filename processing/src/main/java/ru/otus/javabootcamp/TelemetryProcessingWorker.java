package ru.otus.javabootcamp;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TelemetryProcessingWorker {

  private final StreamExecutionEnvironment env;
  private final String server = "localhost:9092";

  TelemetryProcessingWorker(StreamExecutionEnvironment env) {
    this.env = env;
  }

  public void run() throws Exception {

    DataStream<ThresholdViolation> violationStream = env.fromSource(createKafkaConsumer(),
            WatermarkStrategy.noWatermarks(), "Kafka Source")
        .name(String.format("%s kafka stream", "telemetry"))
        .flatMap(new StringToTelemetryPointMapFunction())
        .name("telemetry stream")
        .keyBy(p -> p.username)
        .flatMap(new TelemetryPointToViolationMapFunction())
        .name("violations stream");

    sendData(violationStream);

    env.execute("Telemetry-Processing");
  }


  private void sendData(DataStream<ThresholdViolation> stream) {
    KafkaSink<String> sink = KafkaSink.<String>builder()
        .setBootstrapServers(server)
        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
            .setTopic("telemetry-violation")
            .setValueSerializationSchema(new SimpleStringSchema())
            .build()
        )
        .build();

    stream.map(new MapToStringFunction())
        .sinkTo(sink).name("send to topic");
  }

  public KafkaSource<String> createKafkaConsumer() {
    return KafkaSource.<String>builder()
        .setBootstrapServers(server)
        .setTopics("telemetry")
        .setGroupId("telemetry-processing")
        .setStartingOffsets(OffsetsInitializer.earliest())
        .setValueOnlyDeserializer(new SimpleStringSchema())
        .build();
  }

}
