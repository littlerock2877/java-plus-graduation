package ru.practicum.collector.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.collector.configuration.KafkaUserActionProducer;
import ru.yandex.practicum.grpc.stats.actions.UserActionProto;

@Slf4j
public abstract class BaseUserActionHandler implements UserActionHandler {
    private KafkaUserActionProducer producer;
    private String topic;

    public BaseUserActionHandler(KafkaUserActionProducer kafkaProducer) {
        this.producer = kafkaProducer;
        topic = kafkaProducer.getConfig().getUserActionsTopic();
    }

    @Override
    public void handle(UserActionProto userAction) {
        log.info("Handling user action {}", userAction);
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(
                        topic, null, System.currentTimeMillis(), null, toAvro(userAction));
        log.info("Sending {} to topic {}", userAction, topic);
        producer.sendRecord(record);
    }

    abstract SpecificRecordBase toAvro(UserActionProto userAction);
}