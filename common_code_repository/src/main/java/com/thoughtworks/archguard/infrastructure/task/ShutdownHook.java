package com.thoughtworks.archguard.infrastructure.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    public static void register(long startTime) {
        new ShutdownHook(startTime);
    }

    public ShutdownHook(long start) {
        Thread t = new Thread(() ->
                logger.info("总共运行时间为：{}s", (System.currentTimeMillis() - start) / 1000));
        Runtime.getRuntime().addShutdownHook(t);
    }
}
