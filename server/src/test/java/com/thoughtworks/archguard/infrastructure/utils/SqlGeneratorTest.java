package com.thoughtworks.archguard.infrastructure.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SqlGeneratorTest {

    @Test
    public void shouldGenerateBatchInsertSql() {
        // given
        String table = "users";
        List<Map<String, String>> values = new ArrayList<>();
        Map<String, String> value1 = new HashMap<>();
        value1.put("name", "John");
        value1.put("age", "25");
        values.add(value1);
        Map<String, String> value2 = new HashMap<>();
        value2.put("name", "Jane");
        value2.put("age", "30");
        values.add(value2);

        // when
        String sql = SqlGenerator.generateBatchInsertSql(table, values);

        // then
        String expectedSql = "INSERT INTO `users`(`name`,`age`)  VALUES   ('John','25'), ('Jane','30')";
        assertEquals(expectedSql, sql);
    }

    @Test
    public void shouldGenerateWhere() {
        // given
        Map<String, String> keys = new HashMap<>();
        keys.put("id", "1");
        keys.put("name", "John");

        // when
        String whereClause = SqlGenerator.generateWhere(keys);

        // then
        String expectedWhereClause = " `name`='John'  and  `id`='1' ";
        assertEquals(expectedWhereClause, whereClause);
    }

    @Test
    public void shouldGenerateUpdateString() {
        // given
        String table = "users";
        Map<String, String> keys = new HashMap<>();
        keys.put("id", "1");
        keys.put("name", "John");
        Map<String, String> values = new HashMap<>();
        values.put("age", "30");
        values.put("city", "New York");
        UpdateRecord record = new UpdateRecord(keys, values);

        // when
        String updateString = SqlGenerator.generateUpdateString(table, record);

        // then
        String expectedUpdateString = "UPDATE `users` SET city='New York',age='30' WHERE  `name`='John'  and  `id`='1' ";
        assertEquals(expectedUpdateString, updateString);
    }

    @Test
    public void shouldGenerateUpdateStringOptional() {
        // given
        String table = "users";
        Map<String, String> keys = new HashMap<>();
        keys.put("id", "1");
        keys.put("name", "John");
        Map<String, String> values = new HashMap<>();
        values.put("age", "30");
        values.put("city", "New York");
        UpdateRecord record = new UpdateRecord(keys, values);

        // when
        Optional<String> updateStringOptional = SqlGenerator.generateUpdateStringOptional(table, record);

        // then
        assertTrue(updateStringOptional.isPresent());
        String expectedUpdateString = "UPDATE `users` SET city='New York',age='30' WHERE  `name`='John'  and  `id`='1' ";
        assertEquals(expectedUpdateString, updateStringOptional.get());
    }

    @Test
    public void shouldGenerateEmptyUpdateStringOptional() {
        // given
        String table = "users";
        Map<String, String> keys = new HashMap<>();
        keys.put("id", "1");
        keys.put("name", "John");
        Map<String, String> values = new HashMap<>();
        UpdateRecord record = new UpdateRecord(keys, values);

        // when
        Optional<String> updateStringOptional = SqlGenerator.generateUpdateStringOptional(table, record);

        // then
        assertFalse(updateStringOptional.isPresent());
    }
}

