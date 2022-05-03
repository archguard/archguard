package com.thoughtworks.archguard.infrastructure.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SqlGenerator {
    private SqlGenerator() {
    }

    public static String generateBatchInsertSql(String table, List<Map<String, String>> values) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("INSERT INTO `%s`", table));
        sb.append("(");
        Map<String, String> h = values.get(0);
        StringBuilder fieldBuf = new StringBuilder();
        for (String k : h.keySet()) {
            fieldBuf.append(",");
            fieldBuf.append(String.format("`%s`", k));
        }
        sb.append(fieldBuf.substring(1));
        sb.append(")");
        sb.append("  VALUES  ");

        StringBuilder valuesBuf = new StringBuilder();
        for (Map<String, String> vMap : values) {
            valuesBuf.append(", (");
            StringBuilder vBuf = new StringBuilder();
            for (String v : vMap.values()) {
                vBuf.append(",");
                if (v == null) {
                    vBuf.append((String) null);
                } else if ("true".equals(v)) {
                    vBuf.append(true);
                } else if ("false".equals(v)) {
                    vBuf.append(false);
                } else {
                    vBuf.append(String.format("'%s'", v));
                }
            }
            valuesBuf.append(vBuf.substring(1));
            valuesBuf.append(")");
        }

        sb.append(valuesBuf.substring(1));
        return sb.toString().replaceAll(System.lineSeparator(), "");
    }

    public static String generateWhere(Map<String, String> keys) {
        StringBuilder sb = new StringBuilder();
        keys.forEach((key, value) -> {
            sb.append(" and ");
            sb.append(String.format(" `%s`='%s' ", key, value));
        });
        return sb.substring(5);
    }

    public static String generateUpdateString(String table, UpdateRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("UPDATE `%s` SET ", table));
        StringBuilder valuesBuf = new StringBuilder();
        record.getValues().forEach((key, value) -> {
            valuesBuf.append(",");
            valuesBuf.append(key);
            valuesBuf.append("=");
            valuesBuf.append(String.format("'%s'", value));
        });
        sb.append(valuesBuf.substring(1));
        Map<String, String> keys = record.getKeys();
        String where = generateWhere(keys);
        sb.append(" WHERE ");
        sb.append(where);
        return sb.toString();
    }

    public static Optional<String> generateUpdateStringOptional(String table, UpdateRecord record) {
        if (record.getValues().size() != 0) {
            String string = generateUpdateString(table, record);
            return Optional.of(string);
        }
        return Optional.empty();
    }
}
