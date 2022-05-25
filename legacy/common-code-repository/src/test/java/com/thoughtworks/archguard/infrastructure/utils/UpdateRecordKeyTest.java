package com.thoughtworks.archguard.infrastructure.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateRecordKeyTest {
    @org.junit.jupiter.api.Test
    public void testEqual() {
        java.util.Map<String, String> keyA = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.Map<String, String> keyB = new java.util.concurrent.ConcurrentHashMap<>();
        keyA.put("id-for-key", "id-for-value");
        keyB.put("id-for-key", "id-for-value");
        UpdateRecordKey a = UpdateRecordKey.of(keyA);
        UpdateRecordKey b = UpdateRecordKey.of(keyB);

        assertTrue(a.equals(b));
    }

    @org.junit.jupiter.api.Test
    public void testMapKey() {
        java.util.Map<UpdateRecordKey, String> map = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.Map<String, String> keyA = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.Map<String, String> keyB = new java.util.concurrent.ConcurrentHashMap<>();
        keyA.put("id-for-key", "id-for-value");
        keyB.put("id-for-key", "id-for-value");
        UpdateRecordKey a = UpdateRecordKey.of(keyA);
        UpdateRecordKey b = UpdateRecordKey.of(keyB);

        map.put(a, "value-for-key-a");
        assertTrue(map.containsKey(b));
    }
}
