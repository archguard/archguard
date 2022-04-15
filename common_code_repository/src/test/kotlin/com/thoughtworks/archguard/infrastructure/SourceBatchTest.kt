package com.thoughtworks.archguard.infrastructure

import nl.altindag.log.LogCaptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
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

    @Test
    internal fun should_log_with_execute() {
        val logCaptor: LogCaptor = LogCaptor.forClass(SourceBatch::class.java)

        val batch = SourceBatch()
        val callees: MutableMap<String, String> = HashMap()
        callees["id"] = "123245"
        callees["clzname"] = "clzName"
        callees["a"] = "sourceId"
        callees["b"] = "targetId"
        batch.add("code_method", callees)

        batch.execute()

        assertThat(logCaptor.debugLogs).containsExactly("INSERT INTO `code_method`(`a`,`clzname`,`b`,`id`)  VALUES   ('sourceId','clzName','targetId','123245')")
    }

    @Test
    internal fun should_execute_by_table() {
        val logCaptor: LogCaptor = LogCaptor.forClass(SourceBatch::class.java)

        val batch = SourceBatch()
        val callees: MutableMap<String, String> = HashMap()
        callees["id"] = "123245"
        batch.add("sample", callees)

        batch.executeByTable("sample")

        assertThat(logCaptor.debugLogs).containsExactly("INSERT INTO `sample`(`id`)  VALUES   ('123245')")

        val text = File("sample.sql").readText()
        assert(text.contains("123245"))
    }
}