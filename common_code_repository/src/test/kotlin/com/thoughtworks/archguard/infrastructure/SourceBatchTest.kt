package com.thoughtworks.archguard.infrastructure

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SourceBatchTest {
    @Test
    internal fun should_get_batch_id_when_save() {
        val batch = SourceBatch()

        val callees: MutableMap<String, String> = HashMap()
        callees["id"] = "123245"
        callees["clzname"] = "clzName"
        callees["a"] = "sourceId"
        callees["b"] = "targetId"
        batch.add("code_method", callees)

        val keys: MutableMap<String, String> = HashMap()
        keys["clzname"] = "clzName"
        val optMethod = batch.findId("code_method", keys)


        assertEquals("123245", optMethod.get())
    }
}