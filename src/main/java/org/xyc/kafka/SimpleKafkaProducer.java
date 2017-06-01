package org.xyc.kafka;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2017/2/16
 */
public class SimpleKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleKafkaProducer.class);

    private static final AtomicInteger serialNo = new AtomicInteger();

    private Producer<String, String> producer;

    private SimpleKafkaProducer(Producer<String, String> producer) {
        this.producer = producer;
    }

    public static SimpleKafkaProducer getInstance() {
        return KafkaProducerFactory.simpleKafkaProducer;
    }

    private static String serial() {
        return System.currentTimeMillis() + "-" + serialNo.getAndIncrement();
    }

    public void send(String topic, String message) {
        this.send(topic, serial(), message);
    }

    public void send(String topic, String key, String message) {
        this.producer.send(new ProducerRecord<String, String>(topic, key, message));
        logger.info("topic {} send {}", topic, cutMessage(message));
    }

    private String cutMessage(String message) {
        if (message == null)
            return message;
        int limit = 50;
        if (message.length() <= limit)
            return message;
        return message.substring(0, limit);
    }

    private static class KafkaProducerFactory {

        private static SimpleKafkaProducer simpleKafkaProducer;

        static {
            Properties props = new Properties();
            props.put("acks", "all");
            props.put("retries", 3);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            Properties global = PropertiesLoader.load("producer.properties");

            PropertiesLoader.copy(global, props);

            simpleKafkaProducer = new SimpleKafkaProducer(new KafkaProducer<String, String>(props));
        }
    }
}
