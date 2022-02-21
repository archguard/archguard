package infrastructure.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class UpdateRecordSet {
    Map<String, Map<UpdateRecordKey, UpdateRecordValue>> updateStore = new ConcurrentHashMap<>();

    public void put(String table, Map<String, String> keys, Map<String, String> values) {
        if (!updateStore.containsKey(table)) {
            updateStore.put(table, new ConcurrentHashMap<>());
        }

        UpdateRecordKey k = UpdateRecordKey.of(keys);
        UpdateRecordValue v = UpdateRecordValue.of(values);

        if (updateStore.get(table).containsKey(k)) {
            updateStore.get(table).get(k).addValues(values);
        } else {
            updateStore.get(table).put(k, v);
        }
    }

    public void processEachRecord(BiConsumer<String, UpdateRecord> consumer) {
        updateStore.forEach((table, recordMap) -> {
            recordMap.forEach((key, value) -> {
                consumer.accept(table, UpdateRecord.of(key, value));
            });
        });
    }

    public void clear() {
        updateStore.clear();
    }
}
