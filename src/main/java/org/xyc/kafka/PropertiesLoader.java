package org.xyc.kafka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by wks on date: 2017/2/16
 */
public class PropertiesLoader {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    public static Properties load(String name) {
        Properties p = new Properties();
        load(p, name);
        return p;
    }

    public static void load(Properties p, String name) {
        InputStream is = null;
        try {
            is = PropertiesLoader.class.getClassLoader().getResourceAsStream(name);
            p.load(is);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
        }
    }

    public static void copy(Properties orig, Properties dest) {
        if (orig == null || dest == null)
            return;
        for (String e : orig.stringPropertyNames()) {
            dest.put(e, orig.getProperty(e, dest.getProperty(e)));
        }
    }
}
