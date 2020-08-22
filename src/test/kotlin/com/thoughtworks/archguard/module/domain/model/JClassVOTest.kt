package com.thoughtworks.archguard.module.domain.model

import org.junit.jupiter.api.Assertions.*
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
}