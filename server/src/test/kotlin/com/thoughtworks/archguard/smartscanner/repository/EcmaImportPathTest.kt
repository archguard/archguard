package com.thoughtworks.archguard.smartscanner.repository;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class EcmaImportPathTest {

    @Test
    fun should_return_component_when_convertTypeScriptImport_given_relative_import_path() {
        // given
        val filepath = "src/main.tsx"
        val importPath = "../component"

        // when
        val result = importConvert(filepath, importPath)

        // then
        assertEquals("component", result)
    }

    @Test
    fun should_return_prefixed_component_when_convertTypeScriptImport_given_relative_import_path_with_prefix() {
        // given
        val filepath = "src/main.tsx"
        val importPath = "src/component"

        // when
        val result = importConvert(filepath, importPath)

        // then
        assertEquals("src/component", result)
    }

    @Test
    fun should_return_prefixed_nested_component_when_convertTypeScriptImport_given_relative_import_path_with_nested_prefix() {
        // given
        val filepath = "src/main.tsx"
        val importPath = "@/page/component"

        // when
        val result = importConvert(filepath, importPath)

        // then
        assertEquals("src/page/component", result)
    }

    @Test
    fun should_return_resolved_import_path_when_convertTypeScriptImport_given_relative_import_path_with_file_extension() {
        // given
        val filepath = "src/main.tsx"
        val importPath = "./component"

        // when
        val result = importConvert(filepath, importPath)

        // then
        assertEquals("src/component", result)
    }

    @Test
    fun should_return_resolved_import_path_when_convertTypeScriptImport_given_relative_import_path_with_parent_directory() {
        // given
        val filepath = "src/main.tsx"
        val importPath = "../component"

        // when
        val result = importConvert(filepath, importPath)

        // then
        assertEquals("component", result)
    }
}
