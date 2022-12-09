package com.thoughtworks.archguard.infrastructure;

import org.jetbrains.annotations.TestOnly;
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

public class SourceBatch extends DefaultBatchImpl {
    private static final Logger logger = LoggerFactory.getLogger(SourceBatch.class);
    public static final String[] ALL_TABLES = new String[]{
            "code_class",
            "code_field",
            "code_method",
            "code_annotation",
            "code_annotation_value",

            // code relation refs
            "code_ref_class_fields",
            "code_ref_class_methods",
            "code_ref_class_parent",
            "code_ref_method_fields",
            "code_ref_method_callees",
            "code_ref_class_dependencies",

            // for c4 level in api call
            "container_demand",
            "container_resource",
            "container_service",

            "data_code_database_relation"
    };

    @Override
    public Optional<String> getKey(String table, Map<String, String> values) {
        table = table.toLowerCase();

        switch (table) {
            case "code_method": {
                String key = methodStoreKey(values);
                return Optional.of(key);
            }
            case "code_class": {
                String key = classStoreKey(values);
                return Optional.of(key);
            }
            case "code_field": {
                String key = fieldStoreKey(values);
                return Optional.of(key);
            }
            default:
                return Optional.empty();
        }
    }

    private String fieldStoreKey(Map<String, String> values) {
        return values.get("name") + values.get("clzname");
    }

    private String classStoreKey(Map<String, String> values) {
        if (values.get("module") == null) {
            return values.get("name");
        }
        return values.get("name") + values.get("module");
    }

    private String methodStoreKey(Map<String, String> values) {
        if (values.get("module") == null) {
            return values.get("clzname") + values.get("name") + values.get("argumenttypes");
        }
        return values.get("clzname") + values.get("name") + values.get("module") + values.get("argumenttypes");
    }

    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        int totalInsert = 0;
        for (String table : ALL_TABLES) {
            List<Map<String, String>> values = insertStore.get(table);
            if (values != null && !values.isEmpty()) {
                totalInsert = totalInsert + values.size();
                String sql = com.thoughtworks.archguard.infrastructure.utils.SqlGenerator.generateBatchInsertSql(table, values);
                logger.debug(sql);
                write(sql + ";", table + ".sql");
            }
        }
        insertStore.clear();

        long stop = System.currentTimeMillis();
        logger.info("insert {} finished, spend: {}", totalInsert, (stop - start) / 1000);
    }

    public void executeByTable(String table) {
        long start = System.currentTimeMillis();
        int totalInsert = 0;

        List<Map<String, String>> values = insertStore.get(table);
        if (values != null && !values.isEmpty()) {
            totalInsert = totalInsert + values.size();
            String sql = com.thoughtworks.archguard.infrastructure.utils.SqlGenerator.generateBatchInsertSql(table, values);
            logger.debug(sql);
            write(sql + ";", table + ".sql");
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

    @TestOnly
    public List<Map<String, String>> getStore(String table) {
        return insertStore.get(table);
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
