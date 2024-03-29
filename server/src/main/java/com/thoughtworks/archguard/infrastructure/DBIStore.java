package com.thoughtworks.archguard.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Batch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Phaser;

public class DBIStore {
    private static final Logger logger = LoggerFactory.getLogger(DBIStore.class);

    private static final String JDBC_URL = System.getProperty("dburl");
    private static final DBIStore INSTANCE = new DBIStore(JDBC_URL);
    private final HikariDataSource ds = new HikariDataSource();
    private Jdbi jdbi;

    public static DBIStore getInstance() {
        return INSTANCE;
    }

    private DBIStore(String dburl) {
        this.checkJdbcUrl();
        this.ds.setJdbcUrl(dburl);
        this.ds.setMaximumPoolSize(5);

        disableForeignCheck();
        initConnectionPool();
    }

    public void delete(String table, Phaser phaser, String systemId) {
        com.thoughtworks.archguard.infrastructure.task.SqlExecuteRunnable p = new com.thoughtworks.archguard.infrastructure.task.SqlExecuteRunnable(table, phaser, () ->
                jdbi.withHandle(handle ->
                        handle.execute("DELETE FROM " + table + " WHERE system_id = " + systemId)));

        com.thoughtworks.archguard.infrastructure.task.SqlExecuteThreadPool.execute(p);
    }

    public void save(List<String> sqls, String file, Phaser phaser) {
        int count = 0;
        List<String> buffer;
        do {
            buffer = getSubListBy999(sqls, count);
            doSave(buffer, file + count, phaser);
            count++;
        } while (buffer.size() > 0);
        doSave(buffer, file + count, phaser);
    }

    private void doSave(List<String> sqls, String file, Phaser phaser) {
        if (sqls.isEmpty()) {
            return;
        }

        com.thoughtworks.archguard.infrastructure.task.SqlExecuteRunnable p = new com.thoughtworks.archguard.infrastructure.task.SqlExecuteRunnable(file, phaser, () ->
                jdbi.withHandle(handle -> {
                    Batch batch = handle.createBatch();
                    for (String sql : sqls) {
                        batch.add(sql);
                    }

                    try {
                        batch.execute();
                    } catch (Exception e) {
                        logger.info(String.valueOf(e));
                    }
                    return new ArrayList<Integer>();
                }));

        com.thoughtworks.archguard.infrastructure.task.SqlExecuteThreadPool.execute(p);
    }

    public void fix() {
        com.thoughtworks.archguard.infrastructure.task.SqlExecuteRunnable p = new com.thoughtworks.archguard.infrastructure.task.SqlExecuteRunnable("FIX",
                () -> jdbi.withHandle(handle -> {
                    String sql = "UPDATE code_method, code_class, code_ref_class_methods\n" +
                            "SET code_method.module = code_class.module\n" +
                            "WHERE code_method.id = _code_ref_class_methods.b\n" +
                            "  AND code_class.id = _code_ref_class_methods.a\n" +
                            "  AND code_class.module != code_method.module";
                    return handle.execute(sql);
                }));
        com.thoughtworks.archguard.infrastructure.task.SqlExecuteThreadPool.execute(p);
    }

    public void initConnectionPool() {
        this.jdbi = Jdbi.create(this.ds);
    }

    public void disableForeignCheck() {
        Jdbi disableJdbi = Jdbi.create(JDBC_URL);
        disableJdbi.withHandle(handle -> {
            String sql = "SET global FOREIGN_KEY_CHECKS = 0;";
            return handle.execute(sql);
        });
    }

    public void enableForeignCheck() {
        jdbi.withHandle(handle -> {
            String sql = "SET global FOREIGN_KEY_CHECKS = 1;";
            return handle.execute(sql);
        });
    }

    private void checkJdbcUrl() {
        if (JDBC_URL.isEmpty()) {
            throw new RuntimeException("dburl must be not null or empty");
        }
    }

    private List<String> getSubListBy999(List<String> list, int i) {
        int delta = 999;
        int start = i * delta;
        if (start > list.size()) {
            return Collections.emptyList();
        }
        int end = Math.min(start + delta, list.size());
        return list.subList(start, end);
    }
}
