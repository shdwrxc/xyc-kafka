package org.xyc.kafka;

import org.junit.Test;

/**
 * created by wks on date: 2017/2/16
 */
public class ConsumerTest {

    @Test
    public void testConsume() throws Exception {
        SimpleConsumer.consumeSingleTopic("test", new SomeWorker());
        try {
            Thread.sleep(999999999);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
