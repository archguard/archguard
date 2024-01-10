package com.thoughtworks.archguard.report.domain.models;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MethodVOTest {

    @Test
    fun should_createMethodVO_when_givenFullName() {
        // given
        val fullName = "module1.com.example.module1.package1.Class1.method1(arg1, arg2)"

        // when
        val methodVO = MethodVO.create(fullName)

        // then
        assertEquals("module1", methodVO.moduleName)
        assertEquals("com.example.module1.package1", methodVO.packageName)
        assertEquals("Class1", methodVO.className)
        assertEquals("method1", methodVO.methodName)
        assertEquals("arg1, arg2", methodVO.args)
    }

    @Test
    fun should_createMethodVO_withEmptyArgs_when_givenFullNameWithoutArgs() {
        // given
        val fullName = "module1.com.example.module1.package1.Class1.method1()"

        // when
        val methodVO = MethodVO.create(fullName)

        // then
        assertEquals("module1", methodVO.moduleName)
        assertEquals("com.example.module1.package1", methodVO.packageName)
        assertEquals("Class1", methodVO.className)
        assertEquals("method1", methodVO.methodName)
        assertEquals("", methodVO.args)
    }
}
