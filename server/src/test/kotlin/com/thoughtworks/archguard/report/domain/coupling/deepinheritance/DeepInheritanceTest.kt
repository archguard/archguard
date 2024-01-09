package com.thoughtworks.archguard.report.domain.coupling.deepinheritance;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DeepInheritanceTest {

    @Test
    fun shouldCreateDeepInheritanceObject() {
        // Given
        val id = "1"
        val systemId = 123L
        val moduleName = "Module"
        val packageName = "com.example.package"
        val typeName = "Type"
        val dit = 2

        // When
        val deepInheritance = DeepInheritance(id, systemId, moduleName, packageName, typeName, dit)

        // Then
        assertEquals(id, deepInheritance.id)
        assertEquals(systemId, deepInheritance.systemId)
        assertEquals(moduleName, deepInheritance.moduleName)
        assertEquals(packageName, deepInheritance.packageName)
        assertEquals(typeName, deepInheritance.typeName)
        assertEquals(dit, deepInheritance.dit)
    }

    @Test
    fun shouldCreateDeepInheritanceObjectWithNullModuleName() {
        // Given
        val id = "1"
        val systemId = 123L
        val packageName = "com.example.package"
        val typeName = "Type"
        val dit = 2

        // When
        val deepInheritance = DeepInheritance(id, systemId, null, packageName, typeName, dit)

        // Then
        assertEquals(id, deepInheritance.id)
        assertEquals(systemId, deepInheritance.systemId)
        assertEquals(null, deepInheritance.moduleName)
        assertEquals(packageName, deepInheritance.packageName)
        assertEquals(typeName, deepInheritance.typeName)
        assertEquals(dit, deepInheritance.dit)
    }
}
