package org.xyc.kafka;

import java.util.concurrent.Executors;

/**
 * created by wks on date: 2017/2/17
 */
public class SimpleConsumer {

    public static void consume(final String groupId, final String[] topics, final SimpleConsumerWorker worker) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                SimpleKafkaConsumer consumer = null;
                try {
                    consumer = SimpleKafkaConsumer.getInstance(groupId).subscribe(topics).setWorker(worker);
                    consumer.read();
                } catch (SimpleKafkaException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SimpleKafkaException(e);
                } finally {
                    consumer.close();
                }
            }
        });
    }

    public static void consume(String[] topics, SimpleConsumerWorker worker) {
        consume(null, topics, worker);
    }

    public static void consumeSingleTopic(String groupId, String topic, SimpleConsumerWorker worker) {
        consume(groupId, toArray(topic), worker);
    }

    public static void consumeSingleTopic(String topic, SimpleConsumerWorker worker) {
        consume(toArray(topic), worker);
    }

    public static String[] toArray(String... args) {
        return args;
    }
}
