package com.thoughtworks.archguard.code.module.domain.model

import org.archguard.model.vos.JClassVO
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class JClassVOTest {
    @Test
    internal fun shouldGetPackageNameGivenNameStartWithPackageName() {
        val jClassVO =
            JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service")

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
    internal fun shouldHandleBytecodeDollarSymbol() {
        val jClassVO = JClassVO(
            "buildSrc",
            "Com_thougthworks_archguard_java_conventions_gradle\$2\$inlined\$sam\$i\$org_gradle_api_Action\$0"
        )

        val packageName = jClassVO.getPackageName()

        assertEquals("", packageName)
    }
}
