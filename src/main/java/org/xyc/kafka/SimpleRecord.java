package org.xyc.kafka;

/**
 * TODO: 这里需要写注释
 * Created by dongwt on 2017/3/1.
 */
public class SimpleRecord {

    private String topic;
    private String key;

    private String value;

    public SimpleRecord(String topic, String key, String value) {
        this.setTopic(topic);
        this.setKey(key);
        this.setValue(value);
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
