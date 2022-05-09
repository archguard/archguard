package com.thoughtworks.archguard.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlGeneratorTest {

    @Test
    void shouldGenerateBatchInsertSql() {
        String table = "test";
        java.util.Map<String, String> rowValue = new java.util.HashMap<>();
        rowValue.put("key1", "value1");
        rowValue.put("key2", "value2");
        rowValue.put("key3", null);
        rowValue.put("key4", "true");
        rowValue.put("key5", "false");
        java.util.List<java.util.Map<String, String>> values = java.util.Arrays.asList(rowValue);

        String insertSql = SqlGenerator.generateBatchInsertSql(table, values);

        assertEquals("INSERT INTO `test`(`key1`,`key2`,`key5`,`key3`,`key4`)  VALUES   ('value1','value2',false,null,true)", insertSql);
    }

    @Test
    void shouldGenerateWhere() {
        java.util.Map<String, String> rowValue = new java.util.HashMap<>();
        rowValue.put("key1", "value1");
        rowValue.put("key2", "value2");

        String whereSql = SqlGenerator.generateWhere(rowValue);
        assertEquals(" `key1`='value1'  and  `key2`='value2' ", whereSql);
    }

    @Test
    void shouldUpdateString() {
        java.util.Map<String, String> rowValue = new java.util.HashMap<>();
        rowValue.put("key1", "value1");
        rowValue.put("key2", "value2");

        UpdateRecord updateRecord = new UpdateRecord(rowValue, rowValue);

        String whereSql = SqlGenerator.generateUpdateString("table", updateRecord);
        assertEquals("UPDATE `table` SET key1='value1',key2='value2' WHERE  `key1`='value1'  and  `key2`='value2' ", whereSql);
    }
}