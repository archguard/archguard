package com.thoughtworks.archguard.infrastructure.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SqlExecuteThreadPool {
    private static final SqlExecuteThreadPool instance = new SqlExecuteThreadPool();
    private final ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void execute(Runnable task) {
        instance.pool.execute(task);
    }

    public static void close() {
        instance.pool.shutdown();
    }
}
