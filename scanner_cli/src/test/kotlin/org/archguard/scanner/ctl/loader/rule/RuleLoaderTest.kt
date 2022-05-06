package org.archguard.scanner.ctl.loader.rule

import org.archguard.rule.core.RuleSetProvider
import org.junit.jupiter.api.Test
import java.net.URLClassLoader
import java.util.ServiceLoader
import kotlin.test.assertEquals

internal class RuleLoaderTest {
    @Test
    internal fun load() {
        val jar = this.javaClass.classLoader.getResource("kotlin/testonly-rule_webapi-1.7.0.jar")!!

        val jarUrl = jar.toURI().toURL()

        val urlClassLoader = URLClassLoader(arrayOf(jarUrl))

        val ruleSetProviders = ServiceLoader.load(
            RuleSetProvider::class.java,
            urlClassLoader
        ).toList()

        assertEquals(1, ruleSetProviders.size)
    }
}