package infrastructure.utils;

import java.util.Map;
import java.util.Objects;

public class UpdateRecordKey {
    private Map<String, String> keys;

    public UpdateRecordKey(Map<String, String> keys) {
        this.keys = keys;
    }

    public static UpdateRecordKey of(Map<String, String> keys) {
        return new UpdateRecordKey(keys);
    }

    public Map<String, String> getKeys() {
        return keys;
    }

    @Override
    public boolean equals(Object o) {
        UpdateRecordKey that = (UpdateRecordKey) o;
        return this.keys.equals(that.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keys);
    }
}
