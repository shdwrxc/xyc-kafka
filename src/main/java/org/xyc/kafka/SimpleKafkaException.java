package org.xyc.kafka;

/**
 * created by wks on date: 2017/2/17
 */
public class SimpleKafkaException extends RuntimeException {

    public SimpleKafkaException() {
    }

    public SimpleKafkaException(String message) {
        super(message);
    }

    public SimpleKafkaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpleKafkaException(Throwable cause) {
        super(cause);
    }

    public SimpleKafkaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
