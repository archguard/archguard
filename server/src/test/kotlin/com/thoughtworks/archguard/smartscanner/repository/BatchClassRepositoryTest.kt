package com.thoughtworks.archguard.smartscanner.repository

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.DefaultAsserter.assertNotEquals
import kotlin.test.assertEquals

class BatchClassRepositoryTest {
    @Test
    @Disabled
    fun `should find item by id`() {
        val resource = this.javaClass.classLoader.getResource("regression/jfield_codes.json")!!
        val path = Paths.get(resource.toURI())

        val ds: List<CodeDataStruct> = Json.decodeFromString(path.toFile().readText())

        val repo = BatchClassRepository("1", "java", "path")
        ds.forEach { data -> repo.saveClassItem(data) }
        ds.forEach { data -> repo.saveClassBody(data) }

        val clazz = repo.findClass("com.thoughtworks.archguard.code.clazz.domain.JField", "root")


        assertNotEquals("", clazz.get(), "")

        val mutableMaps = repo.getStore("code_class")

        assertEquals(26, mutableMaps!!.size)
    }
}