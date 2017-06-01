package org.xyc.kafka;

/**
 * created by wks on date: 2017/2/16
 */
public class SimpleProducer {

    private static final SimpleKafkaProducer producer = SimpleKafkaProducer.getInstance();

    public static void send(String topic, String message) {
        producer.send(topic, message);
    }

    public static void send(String topic, String key, String message) {
        producer.send(topic, key, message);
    }
}
