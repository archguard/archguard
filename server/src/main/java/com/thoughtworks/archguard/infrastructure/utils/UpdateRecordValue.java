package com.thoughtworks.archguard.infrastructure.utils;

import java.util.Map;

public class UpdateRecordValue {
    private final Map<String, String> values;

    public Map<String, String> getValues() {
        return values;
    }

    public UpdateRecordValue(Map<String, String> values) {
        this.values = values;
    }

    public void addValues(Map<String, String> values) {
        this.values.putAll(values);
    }

    public static UpdateRecordValue of(Map<String, String> values) {
        return new UpdateRecordValue(values);
    }
}
