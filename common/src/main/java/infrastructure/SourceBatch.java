package infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static infrastructure.utils.SqlGenerator.generateBatchInsertSql;

public class SourceBatch extends DefaultBatchImpl {
    private static final Logger logger = LoggerFactory.getLogger(SourceBatch.class);
    public static final String[] TABLES = new String[]{"JClass", "JField", "JMethod",
            "_ClassFields", "_ClassMethods", "_ClassParent", "_MethodFields", "_MethodCallees", "_ClassDependences", "JAnnotation", "JAnnotationValue"};

    @Override
    public Optional<String> getKey(String table, Map<String, String> values) {
        return Optional.empty();
    }

    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        int totalInsert = 0;
        for (String table : TABLES) {
            List<Map<String, String>> values = insertStore.get(table);
            if (values != null && !values.isEmpty()) {
                totalInsert = totalInsert + values.size();
                String sql = generateBatchInsertSql(table, values);
                logger.debug(sql);
                write(sql + ";", table + ".sql");
            }
        }
        insertStore.clear();

        long stop = System.currentTimeMillis();
        logger.info("insert {} finished, spend: {}", totalInsert, (stop - start) / 1000);
    }

    public static void write(String sql, String path) {
        List<String> ls = new ArrayList<>();
        ls.add(sql);
        write(ls, path);
    }

    public static void write(List<String> sqls, String path) {
        Path file = Paths.get(path);
        try {
            if (file.toFile().createNewFile()) {
                logger.info("create new file: {}", file.getFileName());
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
        try {
            Files.write(file, sqls, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }
}
