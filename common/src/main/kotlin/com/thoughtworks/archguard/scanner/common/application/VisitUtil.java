package com.thoughtworks.archguard.scanner.common.application;

import evolution.common.utils.FileUtil;
import evolution.dependence.infrastructure.DBIStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Phaser;

public class VisitUtil {
    private static final Logger logger = LoggerFactory.getLogger(VisitUtil.class);
    private static final DBIStore store = DBIStore.getInstance();

    private VisitUtil() {
    }

    public static void storeDatabase(String[] tables, String systemId) {
        store.disableForeignCheck();
        store.initConnectionPool();
        logger.info("========================================================");

        Phaser phaser = new Phaser(1);
        deleteByTables(tables, phaser, systemId);
        phaser.arriveAndAwaitAdvance();
        logger.info("============ system {} clean db is done ==============", systemId);

        saveByTables(tables, phaser);
        phaser.arriveAndAwaitAdvance();
        logger.info("============ system {} insert db is done ==============", systemId);

        updateByTables(tables, phaser);
        phaser.arriveAndAwaitAdvance();
        logger.info("============ system {} update db is done ==============", systemId);

        logger.info("========================================================");
        store.enableForeignCheck();
    }

    private static void deleteByTables(String[] tables, Phaser phaser, String systemId) {
        for (String table : tables) {
            long dStart = System.currentTimeMillis();
            store.delete(table, phaser, systemId);
            long dEnd = System.currentTimeMillis();
            logger.info("delete {} spend {}", table, (dEnd - dStart) / 1000);
        }
    }

    private static void saveByTables(String[] tables, Phaser phaser) {
        for (String table : tables) {
            long sStart = System.currentTimeMillis();
            List<String> sqls = FileUtil.read(table + ".sql");
            if (!sqls.isEmpty()) {
                store.save(sqls, table, phaser);
                long sEnd = System.currentTimeMillis();
                logger.info("save table {} with records {} spend {}", table, sqls.size(), (sEnd - sStart) / 1000);
            }
        }
    }

    private static void updateByTables(String[] tables, Phaser phaser) {
        for (String table : tables) {
            long sStart = System.currentTimeMillis();
            List<String> sqls = FileUtil.read(table + ".update.sql");
            if (!sqls.isEmpty()) {
                store.save(sqls, table, phaser);
                long sEnd = System.currentTimeMillis();
                logger.info("update table {} with records {} spend {}", table, sqls.size(), (sEnd - sStart) / 1000);
            }
        }
    }

    public static void cleanSqlFile(String[] tables) {
        cleanInsertSqlFile(tables);
        cleanUpdateSqlFile(tables);
    }

    private static void cleanInsertSqlFile(String[] tables) {
        for (String table : tables) {
            cleanFile(table + ".sql");
        }
    }

    private static void cleanUpdateSqlFile(String[] tables) {
        for (String table : tables) {
            cleanFile(table + ".update.sql");
        }
    }

    private static void cleanFile(String fileName) {
        Path file = Paths.get(fileName);
        if (file.toFile().exists()) {
            logger.info("clean {}", fileName);
            try {
                FileUtil.delete(fileName);
            } catch (IOException e) {
                logger.error("delete {} failed", fileName);
            }
        }
    }
}
