package org.archguard.scanner.ctl.loader.rule

import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleVisitor
import org.archguard.scanner.core.client.dto.ContainerResource
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.net.URLClassLoader
import java.util.ServiceLoader
import kotlin.test.assertEquals

internal class RuleLoaderTest {
    @Test
    @Disabled
    internal fun load() {
        val jar = this.javaClass.classLoader.getResource("kotlin/testonly-rule_webapi-1.7.0.jar")!!

        val jarUrl = jar.toURI().toURL()

        val loader = URLClassLoader(arrayOf(jarUrl))

        val resource = ContainerResource(sourceUrl = "api/book/delete/{bookId}")

        val ruleSetProviders = ServiceLoader.load(RuleSetProvider::class.java, loader).toList()
        val visitor = Class.forName("org.archguard.linter.rule.webapi.WebApiRuleVisitor", true, loader)
            .declaredConstructors[0]
            .newInstance(listOf(resource)) as RuleVisitor

        val issues = visitor.visitor(ruleSetProviders.map { it.get() })

        assertEquals(1, ruleSetProviders.size)
        assertEquals(1, issues.size)
    }
}