package com.thoughtworks.archguard.infrastructure.utils;

import java.util.Map;

public class UpdateRecord {
    private final Map<String, String> keys;
    private final Map<String, String> values;

    public UpdateRecord(Map<String, String> keys, Map<String, String> values) {
        this.keys = keys;
        this.values = values;
    }

    public Map<String, String> getKeys() {
        return keys;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public static UpdateRecord of(UpdateRecordKey key, UpdateRecordValue value) {
        return new UpdateRecord(key.getKeys(), value.getValues());
    }
}
