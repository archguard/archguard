package com.thoughtworks.archguard.scanner.common.infrastructure;

import java.util.Map;
import java.util.Optional;

public class SourceBatch extends DefaultBatchImpl {
    @Override
    public Optional<String> getKey(String table, Map<String, String> values) {
        return Optional.empty();
    }
}
