package infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import infrastructure.task.SqlExecuteRunnable;
import infrastructure.task.SqlExecuteThreadPool;
import io.netty.util.internal.StringUtil;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Batch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Phaser;

public class DBIStore {
    private static final Logger logger = LoggerFactory.getLogger(DefaultBatchImpl.class);

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
    }

    public void delete(String table, Phaser phaser, String systemId) {
        SqlExecuteRunnable p = new SqlExecuteRunnable(table, phaser, () ->
                jdbi.withHandle(handle ->
                        handle.execute("DELETE FROM " + table + " WHERE system_id = " + systemId)));

        SqlExecuteThreadPool.execute(p);
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

        SqlExecuteRunnable p = new SqlExecuteRunnable(file, phaser, () ->
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

        SqlExecuteThreadPool.execute(p);
    }

    public void fix() {
        SqlExecuteRunnable p = new SqlExecuteRunnable("FIX",
                () -> jdbi.withHandle(handle -> {
                    String sql = "update code_method, code_class, code_ref_class_methods\n" +
                            "set code_method.module = code_class.module\n" +
                            "where code_method.id = _code_ref_class_methods.b\n" +
                            "  and code_class.id = _code_ref_class_methods.a\n" +
                            "  and code_class.module != code_method.module";
                    return handle.execute(sql);
                }));
        SqlExecuteThreadPool.execute(p);
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
        if (StringUtil.isNullOrEmpty(JDBC_URL)) {
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
