package org.xyc.kafka;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2017/2/16
 */
public class SimpleKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleKafkaConsumer.class);

    private static final Properties global = new Properties();

    private KafkaConsumer<String, String> consumer;

    private SimpleConsumerWorker worker;

    static {
        PropertiesLoader.load(global, "consumer.properties");
    }

    private SimpleKafkaConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public static SimpleKafkaConsumer getInstance() {
        return new SimpleKafkaConsumer(initKafkaConsumer(null));
    }

    public static SimpleKafkaConsumer getInstance(String groupId) {
        return new SimpleKafkaConsumer(initKafkaConsumer(groupId));
    }

    private static Properties newKafkaConfig() {
        Properties props = new Properties();
        props.put("group.id", "wks");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("session.timeout.ms", "300000");
        props.put("max.poll.records", "10");
        PropertiesLoader.copy(global, props);
        return props;
    }

    private static Properties newKafkaConfig(String groupId) {
        Properties props = newKafkaConfig();
        if (!Strings.isNullOrEmpty(groupId))
            props.setProperty("group.id", groupId);
        return props;
    }

    private static KafkaConsumer<String, String> initKafkaConsumer(String groupId) {
        Properties props = newKafkaConfig(groupId);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        return consumer;
    }

    /**
     * warning：设置topics是一次性的，重新设置会覆盖之前的设置
     *
     * @param topics
     * @return
     */
    public SimpleKafkaConsumer subscribe(String... topics) {
        for (String topic : topics) {
            if (topic == null || topic.trim().isEmpty())
                throw new SimpleKafkaException("topic name cannot be null or empty");
        }
        this.consumer.subscribe(Arrays.asList(topics));
        return this;
    }

    public SimpleKafkaConsumer setWorker(SimpleConsumerWorker worker) {
        this.worker = worker;
        return this;
    }

    public void read() {
        if (this.consumer.listTopics().size() == 0)
            throw new SimpleKafkaException("subscribe at least one topic.");
        if (this.worker == null)
            throw new SimpleKafkaException("worker cannot be null.");
        while (true) {
            ConsumerRecords<String, String> records = this.consumer.poll(5000);
            try {
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    logger.info("topic {} partition {} receive {} records", partition.topic(), partition.partition(), partitionRecords.size());
                    List<SimpleRecord> list = Lists.newArrayList();
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        list.add(new SimpleRecord(record.topic(), record.key(), record.value()));
                    }
                    long l = System.currentTimeMillis();
                    this.worker.consume(list);
                    logger.info("topic {} partition {} done. took {}ms", partition.topic(), partition.partition(), (System.currentTimeMillis() - l));
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    this.consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                    logger.info("topic {} partition {} commit", partition.topic(), partition.partition());
                }
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }
    }

    public void close() {
        if (consumer != null)
            consumer.close();
    }
}
