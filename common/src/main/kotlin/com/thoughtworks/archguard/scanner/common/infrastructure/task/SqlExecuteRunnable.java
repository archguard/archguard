package com.thoughtworks.archguard.scanner.common.infrastructure.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Phaser;

public class SqlExecuteRunnable implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String name;
    private final Runnable task;
    private Phaser phaser;

    public SqlExecuteRunnable(String name, Phaser phaser, Runnable task) {
        this.name = name;
        this.task = task;
        this.phaser = phaser;
        this.phaser.register();
    }

    public SqlExecuteRunnable(String name, Runnable task) {
        this.name = name;
        this.task = task;
    }

    @Override
    public void run() {
        logger.info("thread-{} is running", this.name);
        task.run();
        if (!Objects.isNull(this.phaser)) {
            this.phaser.arriveAndDeregister();
        }
    }
}
