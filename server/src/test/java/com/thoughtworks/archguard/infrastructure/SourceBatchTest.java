package com.thoughtworks.archguard.infrastructure;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SourceBatchTest {

    private static final Logger logger = LoggerFactory.getLogger(SourceBatchTest.class);

    @Test
    public void shouldReturnKeyForCodeMethodTable() {
        // given
        SourceBatch sourceBatch = new SourceBatch();
        Map<String, String> values = new HashMap<>();
        values.put("clzname", "TestClass");
        values.put("name", "testMethod");
        values.put("argumenttypes", "String");

        // when
        Optional<String> key = sourceBatch.getKey("code_method", values);

        // then
        assertTrue(key.isPresent());
        assertEquals("TestClasstestMethodString", key.get());
    }

    @Test
    public void shouldReturnKeyForCodeClassTable() {
        // given
        SourceBatch sourceBatch = new SourceBatch();
        Map<String, String> values = new HashMap<>();
        values.put("name", "TestClass");
        values.put("module", "TestModule");

        // when
        Optional<String> key = sourceBatch.getKey("code_class", values);

        // then
        assertTrue(key.isPresent());
        assertEquals("TestClassTestModule", key.get());
    }

    @Test
    public void shouldReturnKeyForCodeFieldTable() {
        // given
        SourceBatch sourceBatch = new SourceBatch();
        Map<String, String> values = new HashMap<>();
        values.put("name", "testField");
        values.put("clzname", "TestClass");

        // when
        Optional<String> key = sourceBatch.getKey("code_field", values);

        // then
        assertTrue(key.isPresent());
        assertEquals("testFieldTestClass", key.get());
    }

    @Test
    public void shouldReturnEmptyKeyForUnknownTable() {
        // given
        SourceBatch sourceBatch = new SourceBatch();
        Map<String, String> values = new HashMap<>();

        // when
        Optional<String> key = sourceBatch.getKey("unknown_table", values);

        // then
        assertFalse(key.isPresent());
    }

    @Test
    public void shouldWriteSqlToFile() {
        // given
        SourceBatch sourceBatch = new SourceBatch();
        String sql = "INSERT INTO test_table (id, name) VALUES (1, 'test')";
        String path = "test1.sql";

        checkTargetPathExit(path);

        // when
        sourceBatch.write(sql, path);

        // then
        Path file = Paths.get(path);
        assertTrue(file.toFile().exists());
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            assertEquals(1, lines.size());
            assertEquals(sql, lines.get(0));
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private static void checkTargetPathExit(String path) {
        if (Paths.get(path).toFile().exists()) {
            try {
                Files.delete(Paths.get(path));
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    @Test
    public void shouldWriteMultipleSqlsToFile() {
        // given
        SourceBatch sourceBatch = new SourceBatch();
        List<String> sqls = new ArrayList<>();
        sqls.add("INSERT INTO test_table (id, name) VALUES (1, 'test1')");
        sqls.add("INSERT INTO test_table (id, name) VALUES (2, 'test2')");
        String path = "test2.sql";

        // check path if exists remove it
        checkTargetPathExit(path);

        // when
        sourceBatch.write(sqls, path);

        // then
        Path file = Paths.get(path);
        assertTrue(file.toFile().exists());
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            assertEquals(2, lines.size());
            assertEquals(sqls.get(0), lines.get(0));
            assertEquals(sqls.get(1), lines.get(1));
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }
}
