package org.xyc.kafka;

import java.util.List;

/**
 * created by wks on date: 2017/2/16
 */
public interface SimpleConsumerWorker {

    void consume(List<SimpleRecord> list);
}
