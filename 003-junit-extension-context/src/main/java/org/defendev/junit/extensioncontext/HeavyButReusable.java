package org.defendev.junit.extensioncontext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;



public class HeavyButReusable {

    private static final Logger log = LogManager.getLogger();

    private static final Map<String, AtomicInteger> instantiations = new ConcurrentHashMap<>();

    private final String payload;

    public HeavyButReusable(String name) {
        payload = format("This is heavy content of [%s]", name);
        instantiations.computeIfAbsent(name, nameKey -> new AtomicInteger(0)).incrementAndGet();
    }

    public static void logInstantiations() {
        log.info(format(">>--> HeavyButReusable instantiations %s", instantiations.toString()));
    }

    public String getPayload() {
        return payload;
    }

}
