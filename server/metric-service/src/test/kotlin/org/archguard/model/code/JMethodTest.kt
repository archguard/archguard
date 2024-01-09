package org.archguard.model.code;

import org.archguard.config.ConfigType
import org.archguard.config.Configure
import org.archguard.model.vos.JClassVO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JMethodTest {

    @Test
    fun shouldAddType() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))
        val methodType = MethodType.ABSTRACT_METHOD

        // When
        method.addType(methodType)

        // Then
        assertTrue(method.methodTypes.contains(methodType))
    }

    @Test
    fun shouldReturnTrueWhenMethodIsAbstract() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))
        method.addType(MethodType.ABSTRACT_METHOD)

        // When
        val isAbstract = method.isAbstract()

        // Then
        assertTrue(isAbstract)
    }

    @Test
    fun shouldReturnFalseWhenMethodIsNotAbstract() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))

        // When
        val isAbstract = method.isAbstract()

        // Then
        assertFalse(isAbstract)
    }

    @Test
    fun shouldReturnTrueWhenMethodIsSynthetic() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))
        method.addType(MethodType.SYNTHETIC)

        // When
        val isSynthetic = method.isSynthetic()

        // Then
        assertTrue(isSynthetic)
    }

    @Test
    fun shouldReturnFalseWhenMethodIsNotSynthetic() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))

        // When
        val isSynthetic = method.isSynthetic()

        // Then
        assertFalse(isSynthetic)
    }

    @Test
    fun shouldReturnTrueWhenMethodIsStatic() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))
        method.addType(MethodType.STATIC)

        // When
        val isStatic = method.isStatic()

        // Then
        assertTrue(isStatic)
    }

    @Test
    fun shouldReturnFalseWhenMethodIsNotStatic() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))

        // When
        val isStatic = method.isStatic()

        // Then
        assertFalse(isStatic)
    }

    @Test
    fun shouldReturnTrueWhenMethodIsPrivate() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))
        method.addType(MethodType.PRIVATE)

        // When
        val isPrivate = method.isPrivate()

        // Then
        assertTrue(isPrivate)
    }

    @Test
    fun shouldReturnFalseWhenMethodIsNotPrivate() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))

        // When
        val isPrivate = method.isPrivate()

        // Then
        assertFalse(isPrivate)
    }

    @Test
    fun shouldReturnJMethodVO() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))

        // When
        val jMethodVO = method.toVO()

        // Then
        assertEquals("1", jMethodVO.id)
        assertEquals("methodName", jMethodVO.name)
        assertEquals(JClassVO("className", "moduleName"), jMethodVO.clazz)
        assertEquals("returnType", jMethodVO.returnType)
        assertEquals(listOf("arg1", "arg2"), jMethodVO.argumentTypes)
    }

    @Test
    fun shouldBuildColorConfigure() {
        // Given
        val method = JMethod("1", "methodName", "className", "moduleName", "returnType", listOf("arg1", "arg2"))
        val configures = listOf(
            Configure("1", 1, ConfigType.COLOR.typeName, "red", "12", 1),
            Configure("2", 1, ConfigType.COLOR.typeName, "blue", "23", 2),
            Configure("3", 1, ConfigType.DISPLAY.typeName, "small", "34", 3)
        )

        // When
        method.buildColorConfigure(configures)

        // Then
        assertEquals("23", method.configuresMap[ConfigType.COLOR.typeName])
    }
}
