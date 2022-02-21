package com.thoughtworks.archguard.scanner.common.infrastructure.utils;

import java.util.Map;

public class UpdateRecordValue {
    private Map<String, String> values;

    public Map<String, String> getValues() {
        return values;
    }

    public UpdateRecordValue(Map<String, String> values) {
        this.values = values;
    }

    public void addValues(Map<String, String> values) {
        values.forEach((key, value) ->
                // 覆盖
                this.values.put(key, value));
    }

    public static UpdateRecordValue of(Map<String, String> values) {
        return new UpdateRecordValue(values);
    }
}
