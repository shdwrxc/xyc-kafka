package org.xyc.kafka;

import org.junit.Test;

/**
 * created by wks on date: 2017/2/16
 */
public class ProducerTest {

    @Test
    public void testProduce() {
        SimpleProducer.send("test", "hello201702211110");
    }

    @Test
    public void testProduceLoop() {
        for (int i = 0; i < 100; i++) {
            SimpleProducer.send("test", "ClassName", "abc-" + i);
        }
    }
}
