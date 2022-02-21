package com.thoughtworks.archguard.scanner.common.infrastructure;

import com.thoughtworks.archguard.scanner.common.infrastructure.utils.UpdateRecord;
import com.thoughtworks.archguard.scanner.common.infrastructure.utils.UpdateRecordSet;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DefaultBatchImpl implements IBatch {
    private static final Logger logger = LoggerFactory.getLogger(DefaultBatchImpl.class);

    protected final Map<String, String> keyIdStore = new ConcurrentHashMap<>();
    protected final Map<String, List<Map<String, String>>> insertStore = new ConcurrentHashMap<>();
    protected final Map<String, List<UpdateRecord>> updateStore = new ConcurrentHashMap<>();
    protected final UpdateRecordSet updateRecordSet = new UpdateRecordSet();
    protected final Set<String> moduleSet = new ConcurrentSet<>();

    @Override
    public Set<String> getModuleSet() {
        return moduleSet;
    }

    @Override
    public void add(String table, Map<String, String> values) {
        if (!insertStore.containsKey(table)) {
            insertStore.put(table, new ArrayList<>());
        }
        insertStore.get(table).add(values);
        Optional<String> keyOpt = getKey(table, values);
        keyOpt.ifPresent(s -> keyIdStore.put(s, values.get("id")));
        Optional.ofNullable(values.get("module")).ifPresent(moduleSet::add);
    }

    public abstract Optional<String> getKey(String table, Map<String, String> values);

    @Override
    public void update(String table, Map<String, String> keys, Map<String, String> values) {
        updateRecordSet.put(table, keys, values);
    }

    @Override
    public void execute() {
        logger.info("Default Execute: Do nothing now");
    }

    @Override
    public Optional<String> findId(String table, Map<String, String> keys) {
        Optional<String> keyOpt = getKey(table, keys);
        if (keyOpt.isPresent()) {
            String v = keyIdStore.get(keyOpt.get());
            if (v != null) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    @Override
    public void close() {
        logger.info("Default Close: Do nothing now");
    }
}
