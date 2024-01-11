package com.thoughtworks.archguard.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBatchImplTest {

    @Test
    void shouldAddValuesToInsertStoreAndKeyIdStoreAndModuleSet() {
        // given
        DefaultBatchImpl batch = new DefaultBatchImpl() {
            @Override
            public Optional<String> getKey(String table, Map<String, String> values) {
                return Optional.ofNullable(values.get("key"));
            }
        };
        String table = "table";
        Map<String, String> values = new HashMap<>();
        values.put("key", "value");
        values.put("id", "123");
        values.put("module", "module");

        // when
        batch.add(table, values);

        // then
        assertTrue(batch.insertStore.containsKey(table));
        assertEquals(1, batch.insertStore.get(table).size());
        assertEquals(values, batch.insertStore.get(table).get(0));
        assertTrue(batch.keyIdStore.containsKey("value"));
        assertEquals("123", batch.keyIdStore.get("value"));
        assertTrue(batch.moduleSet.contains("module"));
    }

    @Test
    void shouldReturnIdIfExistsInKeyIdStore() {
        // given
        DefaultBatchImpl batch = new DefaultBatchImpl() {
            @Override
            public Optional<String> getKey(String table, Map<String, String> values) {
                return Optional.ofNullable(values.get("key"));
            }
        };
        String table = "table";
        Map<String, String> keys = new HashMap<>();
        keys.put("key", "value");
        batch.keyIdStore.put("value", "123");

        // when
        Optional<String> result = batch.findId(table, keys);

        // then
        assertTrue(result.isPresent());
        assertEquals("123", result.get());
    }

    @Test
    void shouldReturnEmptyOptionalIfIdDoesNotExistInKeyIdStore() {
        // given
        DefaultBatchImpl batch = new DefaultBatchImpl() {
            @Override
            public Optional<String> getKey(String table, Map<String, String> values) {
                return Optional.ofNullable(values.get("key"));
            }
        };
        String table = "table";
        Map<String, String> keys = new HashMap<>();
        keys.put("key", "value");

        // when
        Optional<String> result = batch.findId(table, keys);

        // then
        assertFalse(result.isPresent());
    }
}
