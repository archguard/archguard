package com.thoughtworks.archguard.scanner2.domain.model

import org.archguard.model.vos.JClassVO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class JClassVOTest {
    @Test
    internal fun shouldGetPackageNameGivenNameStartWithPackageName() {
        val jClassVO = JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service")

        val packageName = jClassVO.getPackageName()

        assertEquals("net.aimeizi.dubbo.service.service", packageName)
    }

    @Test
    internal fun shouldGetEmptyPackageNameGivenNameWithoutPackageName() {
        val jClassVO = JClassVO("UserService", "dubbo-service")

        val packageName = jClassVO.getPackageName()

        assertEquals("", packageName)
    }

    @Test
    internal fun shouldGetTypeNameGivenNameStartWithPackageName() {
        val jClassVO = JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service")

        val typeName = jClassVO.getTypeName()

        assertEquals("UserService", typeName)
    }

    @Test
    internal fun shouldGetTypeNameGivenNameNotStartWithPackageName() {
        val jClassVO = JClassVO("UserService", "dubbo-service")

        val typeName = jClassVO.getTypeName()

        assertEquals("UserService", typeName)
    }

    @Test
    internal fun shouldGetBaseClassNameForInternalClass() {
        val jClassVO = JClassVO("JavaParser\$ElementValueContext ", "dubbo-service")
        assertEquals("JavaParser", jClassVO.getBaseClassName())
    }

    @Test
    internal fun shouldGetBaseClassNameForBaseClass() {
        val jClassVO = JClassVO("JavaParser", "dubbo-service")
        assertEquals("JavaParser", jClassVO.getBaseClassName())
    }
}
