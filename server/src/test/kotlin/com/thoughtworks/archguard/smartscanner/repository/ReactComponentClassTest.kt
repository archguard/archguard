package com.thoughtworks.archguard.smartscanner.repository;

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class ReactComponentClassTest {

    @Test
    fun should_return_true_when_filePath_ends_with_tsx() {
        // given
        val filePath = "Component.tsx"

        // when
        val result = ReactComponentClass.isComponent(filePath)

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_return_true_when_filePath_ends_with_jsx() {
        // given
        val filePath = "Component.jsx"

        // when
        val result = ReactComponentClass.isComponent(filePath)

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_return_false_when_filePath_does_not_end_with_tsx_or_jsx() {
        // given
        val filePath = "Component.js"

        // when
        val result = ReactComponentClass.isComponent(filePath)

        // then
        assertEquals(false, result)
    }

    @Test
    fun should_return_ReactComponentClass_with_correct_packageName_and_className_when_from_is_called_with_CodeDataStruct() {
        // given
        val clz = CodeDataStruct()
        clz.Package = "com.thoughtworks.archguard.smartscanner.repository.ReactComponentClass"
        clz.NodeName = "Component"
        clz.FilePath = "Component.tsx"

        // when
        val result = ReactComponentClass.from(clz)

        // then
        assertEquals("com.thoughtworks.archguard.smartscanner.repository.ReactComponentClass", result.packageName)
        assertEquals("Component", result.className)
    }

    @Test
    fun should_return_ReactComponentClass_with_correct_packageName_and_className_when_from_is_called_with_CodeDataStruct_for_index_file() {
        // given
        val clz = CodeDataStruct()
        clz.Package = "com.thoughtworks.archguard.smartscanner.repository.ReactComponentClass.index"
        clz.NodeName = "default"
        clz.FilePath = "Component/index.tsx"
        val function = CodeFunction()
        function.Name = "SomeComponent"
        function.IsReturnHtml = true
        clz.Functions = listOf(function)

        // when
        val result = ReactComponentClass.from(clz)

        // then
        assertEquals("com.thoughtworks.archguard.smartscanner.repository.ReactComponentClass", result.packageName)
        assertEquals("SomeComponent", result.className)
    }

    @Test
    fun should_return_ReactComponentClass_with_correct_packageName_and_className_when_from_is_called_with_CodeDataStruct_for_non_index_file() {
        // given
        val clz = CodeDataStruct()
        clz.Package = "com.thoughtworks.archguard.smartscanner.repository.ReactComponentClass"
        clz.NodeName = "default"
        clz.FilePath = "Component/SomeComponent.tsx"
        val function = CodeFunction()
        function.Name = "SomeComponent"
        function.IsReturnHtml = true
        clz.Functions = listOf(function)

        // when
        val result = ReactComponentClass.from(clz)

        // then
        assertEquals("com.thoughtworks.archguard.smartscanner.repository.ReactComponentClass", result.packageName)
        assertEquals("SomeComponent", result.className)
    }

    @Test
    fun shouldReturnTrueIfFilePathContainsTestPath() {
        // Given
        val filePath =
            "src${File.separator}test${File.separator}com${File.separator}example${File.separator}TestFile.kt"
        val function = CodeFunction()

        // When
        val result = ReactComponentClass.isTest(function, filePath)

        // Then
        assertEquals("true", result)
    }

    @Test
    fun shouldReturnFalseIfFilePathDoesNotContainTestPathAndFunctionIsNotJUnitTest() {
        // Given
        val filePath =
            "src${File.separator}main${File.separator}com${File.separator}example${File.separator}MainFile.kt"
        val function = CodeFunction()

        // When
        val result = ReactComponentClass.isTest(function, filePath)

        // Then
        assertEquals("false", result)
    }

    @Test
    fun shouldConvertTypeScriptImport() {
        // Given
        val importSource = "src/components/Button"
        val filePath = "src/components/App.tsx"

        // When
        val convertedImport = ReactComponentClass.convertTypeScriptImport(importSource, filePath)

        // Then
        assertEquals("@.components.Button", convertedImport)
    }
}
