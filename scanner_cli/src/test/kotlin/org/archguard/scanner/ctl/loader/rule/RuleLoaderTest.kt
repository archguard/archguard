package org.archguard.scanner.ctl.loader.rule

import org.archguard.scanner.core.client.dto.ContainerResource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RuleLoaderTest {
    @Test
    internal fun load() {
        val jar = this.javaClass.classLoader.getResource("kotlin/testonly-rule_webapi-1.7.0.jar")!!

        val resource = ContainerResource(sourceUrl = "api/book/delete/{bookId}")
        val ruleName = "org.archguard.linter.rule.webapi.WebApiRuleVisitor"

        val (ruleVisitor, ruleSetProviders) = RuleLoader.load(listOf(resource), LinterSpec(jar.path, ruleName))

        val issues = ruleVisitor.visitor(ruleSetProviders.map { it.get() })

        assertEquals(1, ruleSetProviders.size)
        assertEquals(1, issues.size)
    }
}