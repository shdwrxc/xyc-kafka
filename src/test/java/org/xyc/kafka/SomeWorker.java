package org.xyc.kafka;

import java.util.List;

/**
 * created by wks on date: 2017/2/16
 */
public class SomeWorker implements SimpleConsumerWorker {

    @Override
    public void consume(List<SimpleRecord> list) {
        for (SimpleRecord record : list) {
            System.out.println(record.getTopic() + "------" + record.getKey() + "------" + record.getValue());
        }
    }
}
